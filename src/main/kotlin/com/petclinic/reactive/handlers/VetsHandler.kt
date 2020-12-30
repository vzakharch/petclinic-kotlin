package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import com.petclinic.reactive.repository.SpecialityRepository
import com.petclinic.reactive.repository.VetRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.renderAndAwait
import java.util.UUID

@Component
class VetsHandler(val vetRepository: VetRepository,
                  val specialityRepository: SpecialityRepository) {

    suspend fun indexPage(serverRequest: ServerRequest) = indexPage()

    suspend fun addPage(serverRequest: ServerRequest) =
            ok().html().renderAndAwait("vets/add", mapOf("specialities" to specialityRepository.findAll().toList()))

    suspend fun add(serverRequest: ServerRequest): ServerResponse = TODO()
//        serverRequest.body(BodyExtractors.toFormData())
//            .flatMap {
//                formData ->
//                vetRepository.save(Vet(
//                        id = UUID.randomUUID().toString(),
//                        firstName = formData["firstName"]?.get(0)!!,
//                        lastNamae = formData["lastName"]?.get(0)!!,
//                        specialities = formData["specialities"]?.toCollection(HashSet())!!))
//            }
//            .then(indexPage())

    fun editPage(serverRequest: ServerRequest): ServerResponse = TODO()
//            vetRepository.findById(
//                    serverRequest.queryParam("id").orElseThrow {IllegalArgumentException()})
//            .map { mapOf("vet" to it, "specialities" to specialityRepository.findAll()) }
//            .flatMap { ok().html().render("vets/edit", it) }

    fun edit(serverRequest: ServerRequest): ServerResponse = TODO()
//        serverRequest.body(BodyExtractors.toFormData())
//            .flatMap { formData ->
//                vetRepository.save(Vet(
//                        id = formData["id"]?.get(0)!!,
//                        firstName = formData["firstName"]?.get(0)!!,
//                        lastName = formData["lastName"]?.get(0)!!,
//                        specialities = formData["specialities"]?.toCollection(HashSet<String>())!!))
//            }
//            .then(indexPage())

    suspend fun indexPage() = ok().html().renderAndAwait("vets/index", mapOf("vets" to vetRepository.findAll().toList()))

}
