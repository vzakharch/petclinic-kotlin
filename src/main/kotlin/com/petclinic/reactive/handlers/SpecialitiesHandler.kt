package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.repository.SpecialityRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.renderAndAwait
import java.util.UUID

@Component
class SpecialitiesHandler(val specialityRepository: SpecialityRepository) {

    suspend fun indexPage(serverRequest: ServerRequest) = indexPage()

    suspend fun addPage(serverRequest: ServerRequest) = ok().html().renderAndAwait("specialities/add")

    suspend fun add(serverRequest: ServerRequest): ServerResponse = TODO()
//            serverRequest.body(BodyExtractors.toFormData())
//                    .flatMap {
//                        val formData = it.toSingleValueMap()
//                        specialityRepository.save(Speciality(
//                                id = UUID.randomUUID().toString(), name = formData["name"]!!))
//                    }
//                    .then(indexPage())

    suspend fun editPage(serverRequest: ServerRequest): ServerResponse = TODO()
//            specialityRepository.findById(
//                    serverRequest.queryParam("id").orElseThrow {IllegalArgumentException()})
//                    .map { mapOf("id" to it.id, "name" to it.name) }
//                    .flatMap { ok().html().render("specialities/edit", it) }

    suspend fun edit(serverRequest: ServerRequest): ServerResponse = TODO()
//            serverRequest.body(BodyExtractors.toFormData())
//                    .flatMap {
//                        val formData = it.toSingleValueMap()
//                        specialityRepository.save(Speciality(
//                                id = formData["id"]!!,
//                                name = formData["name"]!!))
//                    }
//                    .then(indexPage())

    suspend fun indexPage() = ok().html().renderAndAwait("specialities/index",
        mapOf("specialities" to specialityRepository.findAll().toList()))

}
