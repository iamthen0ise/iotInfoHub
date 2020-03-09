package com.github.iamthen0ise

import com.github.iamthen0ise.models.Temperature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.serialization
import kotlinx.serialization.json.json
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

fun Application.module() {
    install(ContentNegotiation) {
        serialization()
    }

    routing {
        get("/") {
            call.respond("ok")
        }

        get("/in/temp") {
            val temperature = call.parameters.get("temp")
            if (temperature != null) {
                val tempDTO = Temperature(
                    DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString(),
                    temperature.toFloat()
                )

                val data = json {
                    "date" to tempDTO.date
                    "value" to tempDTO.value
                }

                File("last.json").writeText(data.toString())
            }

            call.respondText(
                "ok",
                ContentType.Application.Json,
                HttpStatusCode.Created
            )
        }

        get("/api/temp") {
            val temperatureString = File("last.json").readText()
            call.respondText(
                temperatureString,
                ContentType.Application.Json,
                HttpStatusCode.OK
            )
        }
    }
}
