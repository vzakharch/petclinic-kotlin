package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.repository.OwnersRepository
import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.PetTypeRepository
import com.petclinic.reactive.toLocalDate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class PetsHandler(val petRepository: PetRepository,
                  val ownersRepository: OwnersRepository,
                  val petTypeRepository: PetTypeRepository,
                  val ownersHandler: OwnersHandler) {

    fun addPage(serverRequest: ServerRequest) =
            ok().html().render("pets/add", mapOf(
                        "owner" to ownersRepository.findById(
                                serverRequest.queryParam("ownerId").orElseThrow({IllegalArgumentException()})),
                     "petTypes" to petTypeRepository.findAll()))

    fun add(serverRequest: ServerRequest) = serverRequest.body(BodyExtractors.toFormData())
            .flatMap {
                val formData = it.toSingleValueMap()
                petRepository.save(Pet(
                                 name = formData["name"]!!,
                            birthDate = formData["birthDate"]!!.toLocalDate(),
                                owner = formData["ownerId"]!!,
                                 type = formData["typeId"]!!))
            }
            .then(ownersHandler.indexPage())

    fun editPage(serverRequest: ServerRequest) =
            petRepository.findById(serverRequest.queryParam("id").orElseThrow({IllegalArgumentException()}))
                    .map { mapOf("pet" to it,
                          "petTypes" to petTypeRepository.findAll(),
                          "owner" to ownersRepository.findById(it.owner))
                    }
                    .flatMap { ok().html().render("pets/edit", it) }

    fun edit(serverRequest: ServerRequest) = serverRequest.body(BodyExtractors.toFormData())
            .flatMap {
                val formData = it.toSingleValueMap()
                petRepository.save(Pet(
                           id = formData["id"]!!,
                         name = formData["name"]!!,
                    birthDate = formData["birthDate"]!!.toLocalDate(),
                        owner = formData["ownerId"]!!,
                         type = formData["type"]!!))
            }
            .then(ownersHandler.indexPage())

}
