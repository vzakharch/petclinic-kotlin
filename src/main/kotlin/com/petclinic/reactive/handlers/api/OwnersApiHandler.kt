package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.model.Owner
import com.petclinic.reactive.repository.OwnersRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait

@Component
class OwnersApiHandler(val ownersRepository: OwnersRepository) {

    fun getOwners(serverRequest: ServerRequest) =
        ok().body(ownersRepository.findAll(), Owner::class.java)

    suspend fun getOwner(serverRequest: ServerRequest) =
            ok().bodyAndAwait(ownersRepository.findById(serverRequest.pathVariable("id")), Owner::class.java)

}