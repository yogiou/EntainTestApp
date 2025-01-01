package jie.wen.entaintestapp.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseDTO (
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: RaceDataDTO?,
    @SerializedName("message")
    val message: String?
) : Parcelable
