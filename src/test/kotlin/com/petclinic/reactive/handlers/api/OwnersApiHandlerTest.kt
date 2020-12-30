package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.model.Owner
import com.petclinic.reactive.repository.OwnerRepository
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.web.reactive.function.server.EntityResponse
import org.springframework.web.reactive.function.server.ServerRequest

@DataR2dbcTest
@Import(RepoConfiguration::class, OwnersApiHandler::class, OwnerRepository::class)
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@TestPropertySource(properties = ["spring.jpa.generate-ddl=true", "spring.jpa.hibernate.ddl-auto=create"])
internal class OwnersApiHandlerTest(
        @Autowired val ownersApiHandler: OwnersApiHandler,
        @Autowired val ownerRepository: OwnerRepository) {

    val serverRequest: ServerRequest by lazy {
        mock(ServerRequest::class.java)
    }

    @Test
    fun getOwners() = runBlocking {
        val result = (ownersApiHandler.getOwners(serverRequest) as EntityResponse<Flow<Owner>>)
        assertEquals(HttpStatus.OK, result.statusCode())
        val owners = result.entity().toList()
        assertEquals(1, owners.size)
        assertEquals("James", owners[0].firstName)
        assertEquals("Owner", owners[0].lastName)
    }

    @Test
    fun getOwner()  = runBlocking {
        `when`(serverRequest.pathVariable("id")).thenReturn("1")
        val result = (ownersApiHandler.getOwner(serverRequest) as EntityResponse<Owner>)
        assertEquals(HttpStatus.OK, result.statusCode())
        val owner = result.entity()
        assertEquals("James", owner.firstName)
        assertEquals("Owner", owner.lastName)
    }

    @Test
    fun getOwnerNotFound()  = runBlocking {
        `when`(serverRequest.pathVariable("id")).thenReturn("1234")
        val result = ownersApiHandler.getOwner(serverRequest)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode())
    }
}