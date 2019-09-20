package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.model.Visit
import com.petclinic.reactive.repository.PetRepository
import com.petclinic.reactive.repository.VisitRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait

@Component
class PetsApiHandler(val petRepository: PetRepository,
                     val visitRepository: VisitRepository) {

    fun getPets(serverRequest: ServerRequest) =
            ok().body(petRepository.findAll(), Pet::class.java)

    fun getPet(serverRequest: ServerRequest) =
            ok().body(petRepository.findById(serverRequest.pathVariable("id")), Pet::class.java)

    fun getPetVisits(serverRequest: ServerRequest) =
            ok().body(visitRepository.findByPetId(serverRequest.pathVariable("id")), Visit::class.java)

}