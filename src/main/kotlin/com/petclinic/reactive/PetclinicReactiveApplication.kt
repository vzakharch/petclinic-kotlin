package com.petclinic.reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetclinicReactiveApplication

fun main(args: Array<String>) {
	runApplication<PetclinicReactiveApplication>(*args)
}
