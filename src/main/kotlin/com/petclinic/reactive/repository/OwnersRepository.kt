package com.petclinic.reactive.repository

import com.petclinic.reactive.model.Owner
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository interface OwnersRepository : ReactiveMongoRepository<Owner, String>