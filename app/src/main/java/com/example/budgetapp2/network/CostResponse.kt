package com.example.budgetapp2.network

import com.squareup.moshi.Json

data class CostResponse(
    @Json(name = "observations") val observations: List<Observation>
)

data class Observation(
    @Json(name = "value") val value: String
)