package com.petclinic.reactive.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.fold
import org.litote.kmongo.reactivestreams.KMongo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.litote.kmongo.reactivestreams.*  //NEEDED! import KMongo reactivestreams extensions
import org.litote.kmongo.coroutine.* //NEEDED! import KMongo coroutine extensions


@ConfigurationProperties(prefix = "mongodb")
class MongoProperties {
    val database: String? = null
    val user: String? = null
    val password: Int = 0
}

@Component
class MongoClient constructor(
        private val mongoProperties: MongoProperties) {
    val db = KMongo.createClient().getDatabase(mongoProperties.database)
}


interface CoroutinesMongoRepository<T,ID> {
    fun findAll(): Flow<T>
    suspend fun findById(id: ID): T
}

@ExperimentalCoroutinesApi
public suspend fun <T, R> Flow<T>.collectMultimap(
        keyExtractor: (value: T) -> R
): Map<R, Collection<T>> =
        fold(mutableMapOf<R, MutableCollection<T>>())
        { result, element -> addElement(result, element, keyExtractor) }


private fun <T, R> addElement(result: MutableMap<R, MutableCollection<T>>, element: T, keyExtractor: (value: T) -> R): MutableMap<R, MutableCollection<T>> {
    val key = keyExtractor(element)
    val values: MutableCollection<T> = result.getOrDefault(key, mutableListOf())
    values.add(element)
    result[key] = values
    return result
}
