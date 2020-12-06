package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.VisitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class PetsApiHandler(val petRepository: PetRepository,
                     val visitRepository: VisitRepository) {

    suspend fun getPets(serverRequest: ServerRequest) =
        ok().bodyAndAwait(petRepository.findAll())

    suspend fun getPet(serverRequest: ServerRequest): ServerResponse =
        petRepository.findById(serverRequest.pathVariable("id").toLong())
            ?.let { ok().json().bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()

    suspend fun getPetVisits(serverRequest: ServerRequest) =
        ok().bodyAndAwait(visitRepository.findByPetId(serverRequest.pathVariable("id").toLong()))

}