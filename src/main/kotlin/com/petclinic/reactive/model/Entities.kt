package com.petclinic.reactive.model

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class Owner(
    @Id val id:Long? = null,
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
    val telephone: String)

data class Pet(
    @Id val id:Long? = null,
    val name: String,
    val birthDate: LocalDate,
    val type: Long,
    val owner: Long)

data class PetType (@Id val id: Long? = null, val name: String)

data class Speciality(@Id val id:Long? = null, val name: String)

data class Vet(
    @Id val id:Long? = null,
    val firstName: String,
    val lastName : String,
    val specialities: Set<Int> = emptySet())

data class Visit(
    @Id val id:Long? = null,
    val visitDate: LocalDate,
    val description: String,
    val petId: Long)