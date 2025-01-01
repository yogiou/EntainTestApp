package jie.wen.entaintestapp.api

import jie.wen.entaintestapp.model.dto.ResponseDTO
import retrofit2.Response


interface ApiHelper {
    suspend fun fetchNextNRaceSummaryData(
        method: String,
        count: Int
    ) : Response<ResponseDTO>
}