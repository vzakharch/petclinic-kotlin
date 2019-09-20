package com.petclinic.reactive.repository

import com.petclinic.reactive.model.PetType
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository interface PetTypeRepository : ReactiveMongoRepository<PetType, String>