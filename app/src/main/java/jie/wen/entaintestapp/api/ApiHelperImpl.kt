package jie.wen.entaintestapp.api

import jie.wen.entaintestapp.model.dto.ResponseDTO
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
): ApiHelper {
    override suspend fun fetchNextNRaceSummaryData(
        method: String,
        count: Int
    ): Response<ResponseDTO> = apiService.fetchNextNRaceSummaryData(method, count)
}