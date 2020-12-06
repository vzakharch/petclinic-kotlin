package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.model.PetType
import com.petclinic.reactive.repository.PetTypeRepository
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.awaitFormData
import org.springframework.web.reactive.function.server.renderAndAwait

@Component
class PetTypeHandler(val petTypeRepository: PetTypeRepository) {

    suspend fun indexPage(serverRequest: ServerRequest) = indexPage()

    suspend fun addPage(serverRequest: ServerRequest) =
            ok().contentType(MediaType.TEXT_HTML).renderAndAwait("petTypes/add")

    suspend fun add(serverRequest: ServerRequest) =
        serverRequest.awaitFormData().toSingleValueMap().let {
            petTypeRepository.save(PetType(name = it["name"]!!))
            indexPage()
        }

    suspend fun editPage(serverRequest: ServerRequest) =
            petTypeRepository.findById(serverRequest.queryParam("id").orElseThrow{ IllegalArgumentException() })
                    .map { mapOf("id" to it.id, "name" to it.name) }
                    .flatMap { ok().html().render("petTypes/edit", it) }

    suspend fun edit(serverRequest: ServerRequest) =
            serverRequest.body(BodyExtractors.toFormData())
                    .flatMap {
                        val formData = it.toSingleValueMap()
                        petTypeRepository.save(PetType(id = formData["id"]!!, name = formData["name"]!!))
                    }
                    .then(indexPage())

    suspend fun indexPage() = ok().html().renderAndAwait("petTypes/index", mapOf("petTypes" to petTypeRepository.findAll().toList()))

}
