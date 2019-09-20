package com.petclinic.reactive.repository

import com.petclinic.reactive.model.Visit
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository interface VisitRepository : ReactiveMongoRepository<Visit, String> {

    fun findByPetId(petId: String) : Flow<Visit>

}