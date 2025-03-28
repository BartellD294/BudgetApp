package com.example.budgetapp2.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CostResponse(
    @SerialName(value = "observations") val observ: List<Observation>
)

@Serializable
data class Observation(
    @SerialName(value = "date") val date: String,
    @SerialName(value = "value") val cost: String
)