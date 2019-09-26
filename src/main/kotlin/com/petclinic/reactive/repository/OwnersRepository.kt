package com.petclinic.reactive.repository

import com.petclinic.reactive.model.Owner
import org.springframework.stereotype.Repository

@Repository
interface OwnersRepository : CoroutinesMongoRepository<Owner,String>