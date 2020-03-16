package com.github.iamthen0ise

import com.github.iamthen0ise.models.Temperature
import com.github.iamthen0ise.models.TemperatureService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.serialization
import org.joda.time.DateTime
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

fun Application.module() {
    install(ContentNegotiation) {
        serialization()
    }
    install(CORS)
    {
        method(HttpMethod.Options)
        header(HttpHeaders.XForwardedProto)
        anyHost()
        host("localhost")
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }

    DatabaseFactory.init()


    routing {
        static("/static") {
            files("frontend/dist/static/")
        }
        get("/healthcheck") {
            call.respond("ok")
        }

        get("/in/temp") {

            val temperature = call.parameters.get("temp")
            if (temperature != null) {
                val tempDTO = Temperature(
                    id = null,
                    date = DateTime.parse(DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()),
                    value = temperature.toFloat()
                )
                TemperatureService().addTemperature(tempDTO)

            }

            call.respondText(
                "ok",
                ContentType.Application.Json,
                HttpStatusCode.Created
            )
        }

        get("/api/temp") {
            val page = call.parameters.get("page") ?: "1"
            val limit = call.parameters.get("limit") ?: "10"
            call.respond(
                TemperatureService().getAll(page, limit)
            )
        }

        get("/site") {
            call.respondFile(File("frontend/dist/index.html"))
        }
    }


}
