package com.petclinic.reactive.repository

import com.petclinic.reactive.model.Speciality
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository interface SpecialityRepository : CoroutinesMongoRepository<Speciality, String> {

    fun findAllByName(nameList: Iterable<String>) : Flow<Speciality>

}
