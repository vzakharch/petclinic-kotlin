package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.model.Owner
import com.petclinic.reactive.repository.OwnersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait
import reactor.core.publisher.toMono

@Component
class OwnersApiHandler(val ownersRepository: OwnersRepository) {

    @ExperimentalCoroutinesApi
    suspend fun getOwners(serverRequest: ServerRequest) =
        ok().bodyAndAwait(ownersRepository.findAll())

    suspend fun getOwner(serverRequest: ServerRequest) =
            ok().bodyAndAwait(ownersRepository.findById(serverRequest.pathVariable("id")))

}
