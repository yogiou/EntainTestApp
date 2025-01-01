package jie.wen.entaintestapp.data

import jie.wen.entaintestapp.data.enum_data.RaceCategory
import jie.wen.entaintestapp.model.dto.RaceSummaryDTO

data class RaceListState(
    val selectedCategory: RaceCategory = RaceCategory.ALL,
    val raceList: List<RaceSummaryDTO> = emptyList()
)
