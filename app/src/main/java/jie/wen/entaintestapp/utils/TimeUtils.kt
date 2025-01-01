package jie.wen.entaintestapp.utils

import jie.wen.entaintestapp.data.Constants.Companion.ONE_SECOND
import kotlin.math.abs

class TimeUtils {
    companion object {
        fun showCountDownTime (
            currentTime: Long = System.currentTimeMillis() / ONE_SECOND,
            targetTime: Long
        ): String {
            val leadingSign = if (targetTime < currentTime) {
               "-"
            } else {
                ""
            }

            val timeDifferenceInSeconds = abs(currentTime - targetTime)

            val minutes = timeDifferenceInSeconds / 60
            val seconds = timeDifferenceInSeconds % 60

            return if (minutes > 0)
                "$leadingSign${minutes}m ${seconds}s"
            else
                "$leadingSign${seconds}s"
        }
    }
}