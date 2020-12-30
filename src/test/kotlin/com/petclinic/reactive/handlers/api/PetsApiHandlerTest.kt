package com.petclinic.reactive.handlers.api

import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.model.Visit
import com.petclinic.reactive.repository.PetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.web.reactive.function.server.EntityResponse
import org.springframework.web.reactive.function.server.ServerRequest

@SpringBootTest
@TestPropertySource(properties = ["spring.jpa.generate-ddl=true", "spring.jpa.hibernate.ddl-auto=create"])
internal class PetsApiHandlerTest(
        @Autowired val petsApiHandler: PetsApiHandler,
        @Autowired val petsRepository: PetRepository) {

    @Mock
    lateinit var serverRequest: ServerRequest

    @ExperimentalCoroutinesApi
    @Test
    fun getPets() = runBlocking {
        val result = (petsApiHandler.getPets(serverRequest) as EntityResponse<Flow<Pet>>)
        assertEquals(HttpStatus.OK, result.statusCode())
        val pets = result.entity().toList()
        assertEquals(3, pets.size)
        assertEquals("Pet 1", pets[0].name)
        assertEquals("2008-03-23", pets[1].birthDate.toString())
        assertEquals(1, pets[2].owner)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getPetVisits() = runBlocking {
        `when`(serverRequest.pathVariable("id")).thenReturn("1")
        val result = (petsApiHandler.getPetVisits(serverRequest) as EntityResponse<Flow<Visit>>)
        assertEquals(HttpStatus.OK, result.statusCode())
        val visits = result.entity().toList()
        assertEquals(3, visits.size)
        assertThat(visits[0].description.startsWith("Visit description "))
        assertEquals("2019-05-20", visits[1].visitDate.toString())
        assertEquals(3, visits[2].id)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getPet()  = runBlocking {
        `when`(serverRequest.pathVariable("id")).thenReturn("1")
        val result = (petsApiHandler.getPet(serverRequest) as EntityResponse<Pet>)
        assertEquals(HttpStatus.OK, result.statusCode())
        val pet = result.entity()
        assertEquals("Pet 1", pet.name)
        assertEquals("2001-11-23", pet.birthDate.toString())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getPetNotFound()  = runBlocking {
        `when`(serverRequest.pathVariable("id")).thenReturn("1234")
        val result = petsApiHandler.getPet(serverRequest)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode())
    }
}