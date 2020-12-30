package com.petclinic.reactive.repository

import com.petclinic.reactive.model.*
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : CoroutineCrudRepository<Owner, Long> {
    @Query("SELECT DISTINCT owner FROM Owner owner WHERE owner.lastName LIKE :name% OR owner.firstName LIKE :name%")
    fun findByNameLike(name: String): Flow<Owner>
}

@Repository
interface PetRepository : CoroutineCrudRepository<Pet, Long> {
    //@Query("SELECT * FROM Pet WHERE owner = :ownerId")
    suspend fun findAllByOwner(owner: Long): Flow<Pet>
}

@Repository
interface PetTypeRepository : CoroutineCrudRepository<PetType, Long>

@Repository
interface SpecialityRepository : CoroutineCrudRepository<Speciality, Long>

@Repository
interface VetRepository : CoroutineCrudRepository<Vet, Long>

@Repository
interface VisitRepository : CoroutineCrudRepository<Visit,Long> {
    //@Query("SELECT * FROM visit WHERE pet_id = $1")
    fun findByPetId(petId: Long): Flow<Visit>

    //@Query("SELECT * FROM visit WHERE pet_id in (:ids)")
    fun findAllByPetId(ids: Iterable<Long>): Flow<Visit>
}