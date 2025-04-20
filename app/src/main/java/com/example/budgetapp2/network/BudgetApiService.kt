package com.example.budgetapp2.network

import retrofit2.http.GET
import retrofit2.http.Query

interface BudgetApiService {
    @GET("series/observations")
    suspend fun getCosts(
        @Query("series_id") seriesId: String,
        @Query("api_key") apiKey: String,
        @Query("file_type") fileType: String = "json"
    ): CostResponse

    @GET("series/observations")
    suspend fun getLatestCost(
        @Query("series_id") seriesId: String,
        @Query("api_key") apiKey: String,
        @Query("file_type") fileType: String = "json",
        @Query("sort_order") sortOrder: String = "desc",
        @Query("limit") limit: String = "1"
    ): CostResponse

    @GET("series/observations?series_id=GNPCA&api_key=884bbca41956e979caa1e58636c77461&file_type=json")
    suspend fun getGNPCA(): CostResponse

}