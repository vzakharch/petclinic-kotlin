package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.repository.OwnerRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class OwnersApiHandler(val ownerRepository: OwnerRepository) {
    suspend fun getOwners(serverRequest: ServerRequest) =
        ok().json().bodyAndAwait(ownerRepository.findAll())

    suspend fun getOwner(serverRequest: ServerRequest): ServerResponse =
        ownerRepository.findById(serverRequest.pathVariable("id").toLong())
            ?.let{ok().json().bodyValueAndAwait(it)}
            ?: ServerResponse.notFound().buildAndAwait()
}
