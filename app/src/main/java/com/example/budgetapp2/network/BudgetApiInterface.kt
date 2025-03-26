package com.example.budgetapp2.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Json

interface BudgetApiInterface {
    @GET("series/observations")
    suspend fun getCost(
        @Query("series_id") seriesId: String,
        @Query("api_key") apiKey: String,
        @Query("file_type") fileType: String = "json"
    ): Response<CostResponse>

    @GET("series/observations?series_id=GNPCA&api_key=884bbca41956e979caa1e58636c77461&file_type=json")
    suspend fun getGNPCA(): Response<CostResponse>

}