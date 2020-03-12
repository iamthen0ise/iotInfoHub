package com.github.iamthen0ise.models

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

@Serializable
data class Temperature(
    val id: Int?,
    @Serializable(with = DateTimeSerializer::class) val date: DateTime,
    val value: Float
)

object TemperatureData : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val date = datetime("date")
    val value = float("value")
}

object DateTimeSerializer : KSerializer<DateTime> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("DateTimeSerializer")

    override fun serialize(encoder: Encoder, obj: DateTime) {
        encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): DateTime {
        return DateTime(decoder.decodeString())
    }

}