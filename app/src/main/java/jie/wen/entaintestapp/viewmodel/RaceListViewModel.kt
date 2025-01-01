package jie.wen.entaintestapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jie.wen.entaintestapp.api.ApiHelper
import jie.wen.entaintestapp.data.Constants.Companion.MAX_TO_SHOW
import jie.wen.entaintestapp.data.Constants.Companion.METHOD
import jie.wen.entaintestapp.data.Constants.Companion.NEXT_COUNT
import jie.wen.entaintestapp.data.Constants.Companion.ONE_MINUTE
import jie.wen.entaintestapp.data.Constants.Companion.ONE_MINUTE_IN_SECOND
import jie.wen.entaintestapp.data.Constants.Companion.ONE_SECOND
import jie.wen.entaintestapp.data.RaceListState
import jie.wen.entaintestapp.data.enum_data.RaceCategory
import jie.wen.entaintestapp.model.dto.RaceSummaryDTO
import jie.wen.entaintestapp.model.dto.ResponseDTO
import jie.wen.entaintestapp.utils.TimeUtils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RaceListViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {
    val raceSummaryDTOMap = mutableMapOf<RaceCategory, List<RaceSummaryDTO>>()
    var selectedCategory = RaceCategory.ALL

    private lateinit var cache: Response<ResponseDTO>

    private var count = 0

    var uiState by mutableStateOf(RaceListState())
        private set

    override fun onCleared() {
        viewModelScope.takeIf { it.isActive }?.cancel()
        super.onCleared()
    }

    fun fetchData(refreshApiInterval: Long = ONE_MINUTE,
                  refreshTimeInterval: Int) = viewModelScope.launch {
        flow {
            while (true) {
                if (count % refreshTimeInterval == 0 || raceSummaryDTOMap[RaceCategory.ALL]?.isEmpty() == true) {
                    emit(apiHelper.fetchNextNRaceSummaryData(METHOD, NEXT_COUNT))
                } else {
                    emit(cache)
                }

                count++
                delay(refreshApiInterval)
            }
        }.catch { _ ->
            raceSummaryDTOMap.clear()

            uiState = RaceListState(
                selectedCategory,
                raceList = emptyList()
            )
        }.collect { response ->
            cache = response

            raceSummaryDTOMap.clear()

           if (response.isSuccessful) {
                response.body()?.data?.raceSummaries?.let { raceSummaryData ->
                    raceSummaryDTOMap.putAll(process(raceSummaryData, RaceCategory.entries))

                    updateState(emptyList())
                    updateState(raceSummaryDTOMap[selectedCategory]?.take(MAX_TO_SHOW) ?: emptyList())
                 } ?: {
                    updateState(emptyList())
                }
            } else {
               updateState(emptyList())
            }
        }
    }

    fun process(raceData: Map<String, RaceSummaryDTO>,
                filterTypes: List<RaceCategory>): Map<RaceCategory, List<RaceSummaryDTO>> {
        return raceData.values
            .filter { data ->
                !isInValid(data)
            }.let { list ->
                filterTypes.associateWith { type ->
                    when (type) {
                        RaceCategory.ALL -> sort(list)
                        else -> sort(
                            list.filter { it.categoryId == type.id }
                        )
                    }
                }
            }
    }

    fun isInValid(data: RaceSummaryDTO): Boolean {
        return System.currentTimeMillis() / ONE_SECOND - (data.advertisedStart?.seconds ?: 0) > ONE_MINUTE_IN_SECOND
    }

    private fun sort(list: List<RaceSummaryDTO>): List<RaceSummaryDTO> {
        val result = list.sortedBy {
            it.advertisedStart?.seconds
        }

        result.forEach {
            displayCountDownTime(it)
        }

        return result
    }

    private fun displayCountDownTime(raceSummaryDTO: RaceSummaryDTO) {
        raceSummaryDTO.countDown = TimeUtils.showCountDownTime(
            System.currentTimeMillis() / ONE_SECOND,
            raceSummaryDTO.advertisedStart?.seconds ?: 0)
    }

    private fun updateState(list: List<RaceSummaryDTO>) {
        uiState = uiState.copy(
            raceList = list
        )
    }
}
