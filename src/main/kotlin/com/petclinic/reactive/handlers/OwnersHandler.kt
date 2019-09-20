package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.repository.OwnersRepository

import com.petclinic.reactive.model.Owner
import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.model.Visit
import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.PetTypeRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.*
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import reactor.util.function.component1
import reactor.util.function.component2
import java.util.UUID

@Component
class OwnersHandler(val ownersRepository: OwnersRepository,
                    val petRepository: PetRepository,
                    val petTypeRepository: PetTypeRepository,
                    val mongoTemplate: ReactiveMongoTemplate) {

    fun indexPage(serverRequest: ServerRequest) =
            serverRequest.queryParam("q").filter { it.trim().isNotEmpty() }
                    .map { indexPageWithQuery(it) }
                    .orElse(indexPage())

    fun addPage(serverRequest: ServerRequest) = ok().html().render("owners/add")

    fun editPage(serverRequest: ServerRequest) =
            serverRequest.queryParam("id")
                    .map { ownersRepository.findById(it) }
                    .orElse(Mono.empty<Owner>())
                    .map { mapOf("id" to it.id,
                          "firstName" to it.firstName,
                           "lastName" to it.lastName,
                            "address" to it.address,
                               "city" to it.city,
                          "telephone" to it.telephone)
                    }
                    .flatMap { ok().html().render("owners/edit", it) }

    fun view(serverRequest: ServerRequest) =
            serverRequest.queryParam("id").map { ownersRepository.findById(it) }.orElse(Mono.empty<Owner>())
                    .and({ (id) -> petRepository.findAllByOwner(id).collectList() })
                .flatMap { (owner, pets) ->
                    val model = mapOf<String, Any>(
                            "owner" to owner,
                            "pets" to pets,
                            "petTypes" to petTypeRepository.findAll().collectMap({ it.id }, {it.name}),
                            "petVisits" to petVisits(pets.map { it.id }))
                    ok().html().render("owners/view", model)
                }
                .switchIfEmpty(ServerResponse.notFound().build())

    fun petVisits(petIds: List<String>) =
            mongoTemplate.find(Query(where("petId").`in`(petIds) ), Visit::class.java)
                    .collectMultimap { it.petId }

    fun add(serverRequest: ServerRequest) = serverRequest.body(BodyExtractors.toFormData())
            .flatMap {
                val formData = it.toSingleValueMap()
                ownersRepository.save(Owner(
                               id = formData["id"] ?: UUID.randomUUID().toString(),
                        firstName = formData["firstName"]!!,
                         lastName = formData["lastName"]!!,
                          address = formData["address"]!!,
                        telephone = formData["telephone"]!!,
                             city = formData["city"]!!))
            }
            .then(indexPage())

    fun edit(serverRequest: ServerRequest) =
            serverRequest.queryParam("id").map { ownersRepository.findById(it) }.orElse(Mono.empty<Owner>())
                .flatMap { ownersRepository.save(it) }
                .flatMap { ok().render("owners/edit", it) }

    fun indexPageWithQuery(query:String) = ok().html().render("owners/index",
                mapOf("owners" to findByNameLike(query)
                        .map { Pair(it, emptySet<Pet>()) },
                        "pets" to petRepository.findAll().collectMultimap { it.owner }))

    fun indexPage(): Mono<ServerResponse> = ok().html().render("owners/index",
            mapOf("owners" to ownersRepository.findAll().map { Pair(it, emptySet<Pet>()) },
                    "pets" to petRepository.findAll().collectMultimap { it.owner }))

    fun findByNameLike(query:String) = mongoTemplate.find(
            Query(Criteria().orOperator(
                    where("firstName").regex(query, "i"),
                    where("lastName").regex(query, "i"))), Owner::class.java)

}