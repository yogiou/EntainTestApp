package jie.wen.entaintestapp.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RaceDataDTO(
    @SerializedName("next_to_go_ids")
    val nextToGoIds: List<String>?,
    @SerializedName("race_summaries")
    val raceSummaries: Map<String, RaceSummaryDTO>?
) : Parcelable
