package com.github.iamthen0ise.models


import com.github.iamthen0ise.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TemperatureService {
    private suspend fun getTemperature(id: Int): Temperature? = dbQuery {
        TemperatureData.select {
                (TemperatureData.id eq id)
            }.mapNotNull { toTemperature(it) }
            .singleOrNull()
    }


    suspend fun getAll(): ArrayList<Temperature> = dbQuery {
        val messages: ArrayList<Temperature> = arrayListOf()
        transaction {
            TemperatureData.selectAll()
                .orderBy(TemperatureData.date to SortOrder.DESC)
                .limit(10)
                .map {
                    messages.add(
                        Temperature(
                            id = it[TemperatureData.id],
                            date = it[TemperatureData.date],
                            value = it[TemperatureData.value]
                        )
                    )
                }
        }
        messages
    }


    private fun toTemperature(row: ResultRow): Temperature =
        Temperature(
            id = row[TemperatureData.id],
            date = row[TemperatureData.date],
            value = row[TemperatureData.value]
        )


    suspend fun addTemperature(temperature: Temperature): Temperature {
        var key: Int? = 0
        dbQuery {
            key = TemperatureData.insert {
                it[date] = temperature.date
                it[value] = temperature.value
            } get TemperatureData.id
        }
        return getTemperature(key!!)!!
    }
}
