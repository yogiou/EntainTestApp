package jie.wen.entaintestapp.api

import jie.wen.entaintestapp.model.dto.ResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/racing/")
    suspend fun fetchNextNRaceSummaryData(
        @Query("method") method: String,
        @Query("count") count: Int
    ): Response<ResponseDTO>
}