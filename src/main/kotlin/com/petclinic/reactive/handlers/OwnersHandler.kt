package com.petclinic.reactive.handlers

import com.petclinic.reactive.html

import com.petclinic.reactive.model.Owner
import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.repository.OwnerRepository
import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.PetTypeRepository
import com.petclinic.reactive.repository.VisitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.net.URI

class OwnersHandler(val ownerRepository: OwnerRepository,
                    val petRepository: PetRepository,
                    val visitRepository: VisitRepository,
                    val petTypeRepository: PetTypeRepository) {

    suspend fun indexPage(serverRequest: ServerRequest): ServerResponse {
        val param = serverRequest.queryParam("q").filter { it.trim().isNotEmpty() }
        return if (param.isPresent) indexPageWithQuery(param.get()) else indexPage()
    }

    suspend fun addPage(serverRequest: ServerRequest) =
        ok().html().renderAndAwait("owners/add")

    suspend fun editPage(serverRequest: ServerRequest): ServerResponse =
        serverRequest.queryParamOrNull("id")?.toLong()
            ?.let { ownerRepository.findById(it) }
            ?.let {
                mapOf("id" to it.id,
                    "firstName" to it.firstName,
                    "lastName" to it.lastName,
                    "address" to it.address,
                    "city" to it.city,
                    "telephone" to it.telephone)
            }
            ?.let { ok().html().renderAndAwait("owners/edit", it) }
            ?: ok().bodyValueAndAwait(Unit)

    suspend fun view(serverRequest: ServerRequest): ServerResponse =
        serverRequest.queryParamOrNull("id")?.toLong()
            ?.let { ownerRepository.findById(it) }
            ?.let { it to petRepository.findAllByOwner(it.id!!).toList() }
//                    .and({ (id) -> petRepository.findAllByOwner(id).collectList() })
            ?.let { ownerPets ->
                val model = mapOf(
                    "owner" to ownerPets.first,
                    "pets" to ownerPets.second,
                    "petTypes" to petTypeRepository.findAll().map { it.id to it.name }.toSet().toMap(),
                    "petVisits" to petVisits(ownerPets.second.map { it.id!! }))
                ok().html().renderAndAwait("owners/view", model)
            }?: ServerResponse.notFound().buildAndAwait()

    suspend fun petVisits(petIds: List<Long>) =
        visitRepository.findAllByPetId(petIds)
            .toList()
            .groupBy { it.petId }

    suspend fun add(serverRequest: ServerRequest): ServerResponse {
        serverRequest.awaitFormData().toSingleValueMap().let {
            ownerRepository.save(Owner(
                id = it["id"]?.toLong(),
                firstName = it["firstName"]!!,
                lastName = it["lastName"]!!,
                address = it["address"]!!,
                telephone = it["telephone"]!!,
                city = it["city"]!!))
        }
        return indexPage()
    }

    //
//    = serverRequest.body(BodyExtractors.toFormData())
//            .flatMap {
//                val formData = it.toSingleValueMap()
//                ownerRepository.save(Owner(
//                               id = formData["id"] ?: UUID.randomUUID().toString(),
//                        firstName = formData["firstName"]!!,
//                         lastName = formData["lastName"]!!,
//                          address = formData["address"]!!,
//                        telephone = formData["telephone"]!!,
//                             city = formData["city"]!!))
//            }
//            .then(indexPage())
//
    suspend fun edit(serverRequest: ServerRequest): ServerResponse {
        val form = serverRequest.awaitFormData().toSingleValueMap()
        return Owner(id = form["id"]?.toLong(),
            firstName = form["firstName"]!!,
            lastName = form["lastName"]!!,
            address = form["address"]!!,
            telephone = form["telephone"]!!,
            city = form["city"]!!)
//                    ?.let {
//                        println("1>>>> result: $it")
//                        ownerRepository.findById(it)
//                    }
            .let {
                println("2>>>> result: $it")
                ownerRepository.save(it)
            }?.let {
                println("3>>>> result: $it")
                ServerResponse.permanentRedirect(URI.create("/owners/view?id=${it.id}")).buildAndAwait()
            } // ok().renderAnyAndAwait("owners/edit", it) }
            ?: (ok().bodyValueAndAwait("test"))
    }

    suspend fun indexPageWithQuery(query: String) = ok().html().renderAndAwait("owners/index",
        mapOf("owners" to ownerRepository.findByNameLike(query)
            .map { Pair(it, emptySet<Pet>()) },
            "pets" to petRepository.findAll().collectMultimap { it.owner }))

    suspend fun indexPage(): ServerResponse = ok().html().renderAndAwait("owners/index",
        mapOf("owners" to ownerRepository.findAll().map { Pair(it, emptySet<Pet>()) },
            "pets" to petRepository.findAll().collectMultimap { it.owner }))
//
//

    private suspend fun <T, K> Flow<T>.collectMultimap(keySelector: (T) -> K): Map<K, List<T>> {
        return this
            .toList()
            .groupBy(keySelector)
    }

}

