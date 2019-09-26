package com.petclinic.reactive.repository

import com.petclinic.reactive.model.Pet
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Repository

@Repository interface PetRepository : CoroutinesMongoRepository<Pet,String> {

    fun findAllByOwner(id: String): Flow<Pet>

}
