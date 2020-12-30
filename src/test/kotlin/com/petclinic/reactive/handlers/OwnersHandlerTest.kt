package com.petclinic.reactive.handlers

import com.petclinic.reactive.repository.PetRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.web.reactive.function.server.RenderingResponse
import org.springframework.web.reactive.function.server.ServerRequest

@SpringBootTest
@TestPropertySource(properties = ["spring.jpa.generate-ddl=true", "spring.jpa.hibernate.ddl-auto=create"])
internal class OwnersHandlerTest(
        @Autowired val ownersHandler: OwnersHandler,
        @Autowired val petRepository: PetRepository) {

    @Mock
    lateinit var serverRequest: ServerRequest

    @Test
    fun addPage() = runBlocking {
        val result = ownersHandler.addPage(serverRequest) as RenderingResponse
        assertEquals(HttpStatus.OK, result.statusCode())
    }

    @Test
    fun petVisits() = runBlocking {
        val pets = petRepository.findAll().map{it.id!!}.toList()

        val result = ownersHandler.petVisits(pets)

        assertEquals(2, result.size, "Total number of pets with visit")
        assertEquals(3, result[1]?.size, "Pet 1 visits")
        assertEquals(1, result[2]?.size, "Pet 2 visits")
    }
}