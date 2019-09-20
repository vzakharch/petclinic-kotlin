package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.model.PetType
import com.petclinic.reactive.repository.PetTypeRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class PetTypeHandler(val petTypeRepository: PetTypeRepository) {

    fun indexPage(serverRequest: ServerRequest) = indexPage()

    fun addPage(serverRequest: ServerRequest) =
            ok().contentType(MediaType.TEXT_HTML).render("petTypes/add")

    fun add(serverRequest: ServerRequest) = serverRequest.body(BodyExtractors.toFormData())
            .flatMap {
                val formData = it.toSingleValueMap()
                petTypeRepository.save(PetType(name = formData["name"]!!))
            }
            .then(indexPage())

    fun editPage(serverRequest: ServerRequest) =
            petTypeRepository.findById(serverRequest.queryParam("id").orElseThrow{ IllegalArgumentException() })
                    .map { mapOf("id" to it.id, "name" to it.name) }
                    .flatMap { ok().html().render("petTypes/edit", it) }

    fun edit(serverRequest: ServerRequest) =
            serverRequest.body(BodyExtractors.toFormData())
                    .flatMap {
                        val formData = it.toSingleValueMap()
                        petTypeRepository.save(PetType(id = formData["id"]!!, name = formData["name"]!!))
                    }
                    .then(indexPage())

    fun indexPage() = ok().html().render("petTypes/index", mapOf("petTypes" to petTypeRepository.findAll()))

}
