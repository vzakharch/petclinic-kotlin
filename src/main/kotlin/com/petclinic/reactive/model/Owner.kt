package com.petclinic.reactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Owner(@Id val id:String,
                 val firstName: String,
                 val lastName: String,
                 val address: String,
                 val city: String,
                 val telephone: String)

