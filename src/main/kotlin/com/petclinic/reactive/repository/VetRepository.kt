package com.petclinic.reactive.repository

import com.petclinic.reactive.model.Vet
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository interface VetRepository : CoroutinesMongoRepository<Vet, String>