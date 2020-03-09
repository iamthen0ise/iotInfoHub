package com.github.iamthen0ise.models

import kotlinx.serialization.Serializable

@Serializable
data class Temperature(
    val date: String,
    val value: Float
)