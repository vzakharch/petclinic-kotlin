package com.petclinic.reactive.handlers.api

import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories("com.petclinic.reactive.repository")
class RepoConfiguration {

}
