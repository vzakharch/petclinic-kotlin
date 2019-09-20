package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.model.Speciality
import com.petclinic.reactive.repository.SpecialityRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.util.UUID

@Component
class SpecialitiesHandler(val specialityRepository: SpecialityRepository) {

    fun indexPage(serverRequest: ServerRequest) = indexPage()

    fun addPage(serverRequest: ServerRequest) = ok().html().render("specialities/add")

    fun add(serverRequest: ServerRequest) =
            serverRequest.body(BodyExtractors.toFormData())
                    .flatMap {
                        val formData = it.toSingleValueMap()
                        specialityRepository.save(Speciality(
                                id = UUID.randomUUID().toString(), name = formData["name"]!!))
                    }
                    .then(indexPage())

    fun editPage(serverRequest: ServerRequest) =
            specialityRepository.findById(
                    serverRequest.queryParam("id").orElseThrow {IllegalArgumentException()})
                    .map { mapOf("id" to it.id, "name" to it.name) }
                    .flatMap { ok().html().render("specialities/edit", it) }

    fun edit(serverRequest: ServerRequest) =
            serverRequest.body(BodyExtractors.toFormData())
                    .flatMap {
                        val formData = it.toSingleValueMap()
                        specialityRepository.save(Speciality(
                                id = formData["id"]!!,
                                name = formData["name"]!!))
                    }
                    .then(indexPage())

    fun indexPage() = ok().html().render("specialities/index",
            mapOf("specialities" to specialityRepository.findAll()))

}
