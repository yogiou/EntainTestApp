package jie.wen.entaintestapp.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RaceSummaryDTO (
    @SerializedName("race_id")
    val raceId: String? = null,
    @SerializedName("race_number")
    val raceNumber: Int? = null,
    @SerializedName("meeting_name")
    val meetingName: String? = null,
    @SerializedName("advertised_start")
    val advertisedStart: AdvertisedStart? = null,
    @SerializedName("category_id")
    val categoryId: String? = null,
    @SerializedName("race_name")
    val raceName: String? = null,
    @SerializedName("meeting_id")
    val meetingId: String? = null,
    @SerializedName("venue_id")
    val venueId: String? = null,
    @SerializedName("venue_name")
    val venueName: String? = null,
    @SerializedName("venue_state")
    val venueState: String? = null,
    @SerializedName("venue_country")
    val venueCountry: String? = null,
    @SerializedName("race_form")
    val raceFrom: RaceForm? = null,
    var countDown: String? = null
) : Parcelable

@Parcelize
data class AdvertisedStart(
    @SerializedName("seconds")
    val seconds: Long
) : Parcelable

@Parcelize
data class RaceForm(
    @SerializedName("distance")
    val distance: Int? = null,
    @SerializedName("distance_type_id")
    val distanceTypeId: String? = null,
    @SerializedName("track_condition_id")
    val trackConditionId: String? = null,
    @SerializedName("race_comment")
    val raceComment: String? = null,
    @SerializedName("additional_data")
    val additionalData: String? = null,
    @SerializedName("generated")
    val generated: Int? = null,
    @SerializedName("silk_base_url")
    val silkBaseUrl: String? = null,
    @SerializedName("race_comment_alternative")
    val raceCommentAlternative: String? = null,
    @SerializedName("distance_type")
    val distanceType: DistanceType? = null,
    @SerializedName("track_condition")
    val trackCondition: TrackCondition? = null,
    @SerializedName("weather_id")
    val weatherId: String? = null,
    @SerializedName("weather")
    val weather: Weather? = null
) : Parcelable

@Parcelize
data class DistanceType(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("short_name")
    val shortName: String? = null
) : Parcelable

@Parcelize
data class TrackCondition(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("short_name")
    val shortName: String? = null
) : Parcelable

@Parcelize
data class Weather(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("short_name")
    val shortName: String? = null,
    @SerializedName("icon_uri")
    val iconUri: String? = null
) : Parcelable