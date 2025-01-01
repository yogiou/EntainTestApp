package jie.wen.entaintestapp.utils

import org.junit.Assert
import org.junit.Test

class TimeUtilsTest {
    @Test
    fun testShowCountDownTime() {
        Assert.assertNotNull(
            TimeUtils.showCountDownTime(System.currentTimeMillis() / 1000, 1735473660)
        )

        val now = System.currentTimeMillis() / 1000
        Assert.assertEquals(
            "3s",
            TimeUtils.showCountDownTime(now, now + 3)
        )

        Assert.assertEquals(
            "-4s",
            TimeUtils.showCountDownTime(now, now - 4)
        )

        Assert.assertEquals(
            "-1m 1s",
            TimeUtils.showCountDownTime(now, now - 61)
        )

        Assert.assertEquals(
            "2m 50s",
            TimeUtils.showCountDownTime(now, now + 170)
        )

        Assert.assertEquals(
            "62m 50s",
            TimeUtils.showCountDownTime(now, now + 170 + 3600)
        )
    }
}