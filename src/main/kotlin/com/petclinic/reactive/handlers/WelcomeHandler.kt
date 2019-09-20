package com.petclinic.reactive.handlers

import com.petclinic.reactive.html
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.renderAndAwait

@Component
class WelcomeHandler {
    suspend fun welcome(serverRequest: ServerRequest): ServerResponse = ok().html().renderAndAwait("welcome")
}