package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.repository.OwnerRepository
import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.PetTypeRepository
import com.petclinic.reactive.toLocalDate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class PetsHandler(val petRepository: PetRepository,
                  val ownerRepository: OwnerRepository,
                  val petTypeRepository: PetTypeRepository,
                  val ownersHandler: OwnersHandler) {


    suspend fun addPage(serverRequest: ServerRequest): ServerResponse =
        serverRequest.queryParamOrNull("ownerId")?.toLong()
            ?.let {
                ok().html().renderAndAwait("pets/add", mapOf(
                    "owner" to ownerRepository.findById(it),
                    "petTypes" to petTypeRepository.findAll()))
            } ?: ServerResponse.badRequest().buildAndAwait()

    suspend fun add(serverRequest: ServerRequest): ServerResponse =
        serverRequest.awaitFormData().toSingleValueMap().let {
            petRepository.save(Pet(
                name = it["name"]!!,
                birthDate = it["birthDate"]?.toLocalDate()!!,
                owner = it["ownerId"]?.toLong()!!,
                type = it["typeId"]?.toLong()!!))
        }.let { ownersHandler.indexPage() }

    suspend fun editPage(serverRequest: ServerRequest): ServerResponse = TODO()
//            petRepository.findById(serverRequest.queryParam("id").orElseThrow({IllegalArgumentException()}))
//                    .map { mapOf("pet" to it,
//                            "petTypes" to petTypeRepository.findAll(),
//                            "owner" to ownersRepository.findById(it.owner))
//                    }
//                    .flatMap { ok().html().render("pets/edit", it) }

    suspend fun edit(serverRequest: ServerRequest): ServerResponse = TODO()
//            serverRequest.body(BodyExtractors.toFormData())
//            .flatMap {
//                val formData = it.toSingleValueMap()
//                petRepository.save(Pet(
//                        id = formData["id"]!!,
//                        name = formData["name"]!!,
//                        birthDate = formData["birthDate"]!!.toLocalDate(),
//                        owner = formData["ownerId"]!!,
//                        type = formData["type"]!!))
//            }
//            .then(ownersHandler.indexPage())

}

