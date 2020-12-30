package com.petclinic.reactive.handlers

import com.petclinic.reactive.model.Owner
import com.petclinic.reactive.model.Pet
import com.petclinic.reactive.model.PetType
import com.petclinic.reactive.model.Visit
import com.petclinic.reactive.repository.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.util.*

//@Component
class DbInitializer(val petTypeRepository: PetTypeRepository,
                    val specialityRepository: SpecialityRepository,
                    val vetRepository: VetRepository,
                    val ownerRepository: OwnerRepository,
                    val petRepository: PetRepository,
                    val visitRepository: VisitRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {

        runBlocking {

            //        val ownerId = UUID.fromString("5bead0d3-cd7b-41e5-b064-09f48e5e6a08").toString()
//        val petId = UUID.fromString("6bead0d3-cd7b-41e5-b064-09f48e5e6a08").toString()
//        val secondPetId = UUID.fromString("6bead0d2-cd7b-41e5-b064-09f48e5e6a08").toString()
//        val thirdPetId = UUID.fromString("6bead0a3-cd7b-41e5-b064-09f48e5e6a08").toString()
//        val dogId = UUID.randomUUID().toString()

            petTypeRepository.deleteAll()

            val petTypes =
                    petTypeRepository.saveAll(listOf("cat", "lizard", "snake", "bird", "hamster", "dog").map { PetType(name = it) })
                            .toList().map { it.name to it.id!! }.toMap()


//
//
//        specialityRepository.deleteAll().subscribeOnComplete {
//            val specialities = listOf("radiology", "dentistry", "surgery")
//                    .map { Speciality(name = it) }
//            specialityRepository.saveAll(specialities)
//                    .subscribeOnComplete { println("Added  Specialities") }
//        }

//        vetRepository.deleteAll().subscribeOnComplete {
//            vetRepository.saveAll(listOf(
//                    Vet(firstName = "James", lastName="Carter"),
//                    Vet(firstName = "Helen", lastName="Leary", specialities = setOf("radiology")),
//                    Vet(firstName = "Linda", lastName="Douglas", specialities = setOf("dentistry", "surgery")),
//                    Vet(firstName = "Rafael", lastName="Ortega", specialities = setOf("surgery")),
//                    Vet(firstName = "Henry", lastName="Stevens", specialities = setOf("radiology")),
//                    Vet(firstName = "Sharon", lastName="Jenkins")))
//                    .subscribeOnComplete { println("Added  Vets") }
//        }

            ownerRepository.deleteAll()

            val owners = ownerRepository.saveAll(listOf(
                    Owner(firstName = "James", lastName = "Owner",
                            telephone = "+44 4444444", address = "Road St",
                            city = "Serverless")))
                    .toList()


            petRepository.deleteAll()

            val pets = petRepository.saveAll(listOf(
                    Pet(name = "Pet 1", birthDate = LocalDate.of(2001,11,23), type = petTypes["dog"]!!, owner = owners[0].id!!),
                    Pet(name = "Pet 2", birthDate = LocalDate.of(2008,3,23), type = petTypes["cat"]!!, owner = owners[0].id!!),
                    Pet(name = "Pet 3", birthDate = LocalDate.of(2011,9,1), type = petTypes["dog"]!!, owner = owners[0].id!!)))
                    .toList()


            visitRepository.deleteAll()

            visitRepository.saveAll(listOf(
                    Visit(visitDate = LocalDate.of(2020,3,23), description = "Visit description ${Random().nextInt()}", petId = pets[0].id!!),
                    Visit(visitDate = LocalDate.of(2019,5,20), description = "Visit description ${Random().nextInt()}", petId = pets[0].id!!),
                    Visit(visitDate = LocalDate.of(2018,7,17), description = "Visit description ${Random().nextInt()}", petId = pets[0].id!!),
                    Visit(visitDate = LocalDate.of(2017,9,14), description = "Visit description ${Random().nextInt()}", petId = pets[1].id!!)))
                    .collect()
        }
    }

    /**
     * Subscribe onComplete only. Used by db populators.
     */
    private fun <T> Mono<T>.subscribeOnComplete(completeConsumer: () -> Unit): Disposable {
        return this.subscribe(null, null, completeConsumer)
    }

    private fun <T> Flux<T>.subscribeOnComplete(completeConsumer: () -> Unit): Disposable {
        return this.subscribe(null, null, completeConsumer)
    }
}


