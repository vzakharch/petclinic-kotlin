package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.VisitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait

@Component
class PetsApiHandler(val petRepository: PetRepository,
                     val visitRepository: VisitRepository) {

    @ExperimentalCoroutinesApi
    suspend fun getPets(serverRequest: ServerRequest) =
            ok().bodyAndAwait(petRepository.findAll())

    suspend fun getPet(serverRequest: ServerRequest) =
            ok().bodyAndAwait(petRepository.findById(serverRequest.pathVariable("id")))

    @ExperimentalCoroutinesApi
    suspend fun getPetVisits(serverRequest: ServerRequest) =
            ok().bodyAndAwait(visitRepository.findByPetId(serverRequest.pathVariable("id")))

}