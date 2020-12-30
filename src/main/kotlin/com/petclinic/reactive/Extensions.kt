package com.petclinic.reactive

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_EVENT_STREAM
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.ServerResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun ServerResponse.BodyBuilder.json() = contentType(APPLICATION_JSON)

fun ServerResponse.BodyBuilder.textEventStream() = contentType(TEXT_EVENT_STREAM)

fun ServerResponse.BodyBuilder.html() = contentType(TEXT_HTML)

// Date Extension methods
fun LocalDate.toStr(format:String = "dd/MM/yyyy") = DateTimeFormatter.ofPattern(format).format(this)

fun String.toLocalDate(format:String = "dd/MM/yyyy") = LocalDate.parse(this, DateTimeFormatter.ofPattern(format))

suspend fun ServerResponse.BodyBuilder.renderAnyAndAwait(name: String, vararg modelAttributes: Any): ServerResponse =
    render(name, *modelAttributes).awaitSingle()

