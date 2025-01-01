package jie.wen.entaintestapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import jie.wen.entaintestapp.api.ApiHelper
import jie.wen.entaintestapp.data.Constants.Companion.METHOD
import jie.wen.entaintestapp.data.Constants.Companion.NEXT_COUNT
import jie.wen.entaintestapp.data.enum_data.RaceCategory
import jie.wen.entaintestapp.model.dto.AdvertisedStart
import jie.wen.entaintestapp.model.dto.RaceDataDTO
import jie.wen.entaintestapp.model.dto.RaceSummaryDTO
import jie.wen.entaintestapp.model.dto.ResponseDTO
import jie.wen.entaintestapp.utils.TimeUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class RaceListViewModelTest {
    private var testDispatcher = StandardTestDispatcher()

    @Rule @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun testFetchData() = runTest(testDispatcher) {
        val apiHelper = mockk<ApiHelper>()
        val response = ResponseDTO(
            200,
            RaceDataDTO(listOf("84578e96-aa35-44d5-a42f-895ef42132a8"), mutableMapOf(
                "84578e96-aa35-44d5-a42f-895ef42132a8" to RaceSummaryDTO(
                    "32416b1d-5ff6-4218-b198-5853e576a8a6",
                    4,
                    "Sunderland Bags",
                    AdvertisedStart(1735473660),
                    "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
                ),
            )),
            "success"
        )

        val success = Response.success(200, response)
        val raceListViewModel = RaceListViewModel(apiHelper)

        launch {
            coEvery { apiHelper.fetchNextNRaceSummaryData(METHOD, NEXT_COUNT) } returns success
            raceListViewModel.fetchData(10, 1)
            coVerify(atLeast = 0) { apiHelper.fetchNextNRaceSummaryData(METHOD, NEXT_COUNT) }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testFetchDataFail() = runTest(testDispatcher) {
        val apiHelper = mockk<ApiHelper>()
        val errorResponse : ResponseBody = ByteArray(0).toResponseBody(null)
        val raceListViewModel = RaceListViewModel(apiHelper)
        val failResponse = Response.error<ResponseDTO>(400, errorResponse)

        launch {
            coEvery { apiHelper.fetchNextNRaceSummaryData(METHOD, NEXT_COUNT) } returns failResponse
            raceListViewModel.fetchData(10, 1)
            delay(1000)
            coVerify(atLeast =  0) { apiHelper.fetchNextNRaceSummaryData(METHOD, NEXT_COUNT) }
            Assert.assertEquals(0, raceListViewModel.raceSummaryDTOMap.size)
        }
    }

    @Test
    fun testFilterInvalidData() {
        val now = System.currentTimeMillis() / 1000

        val list = listOf(
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a856",
                4,
                "Sunderland Bags",
                AdvertisedStart(now - 1235999),
                "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
            ),
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a8a6",
                2,
                "Sunderland Bags",
                AdvertisedStart(now  + 1000),
                "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
            )
        )

        val apiHelper = mockk<ApiHelper>()
        val raceListViewModel = RaceListViewModel(apiHelper)
        val result = list.filter { !raceListViewModel.isInValid(it) }

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(listOf(
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a8a6",
                2,
                "Sunderland Bags",
                AdvertisedStart(now  + 1000),
                "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
            )
        ), result)


        val list2 = listOf(
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a856",
                4,
                "Sunderland Bags",
                AdvertisedStart(now+ 1235999),
                "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
            ),
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a8a6",
                2,
                "Sunderland Bags",
                AdvertisedStart(now  + 1000),
                "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
            )
        )

        val result2 = list2.filter { !raceListViewModel.isInValid(it) }
        Assert.assertEquals(2, result2.size)
    }

    @Test
    fun testProcess() {
        val now = System.currentTimeMillis() / 1000

        val map = mutableMapOf(
            "32416b1d-5ff6-4218-b198-5853e576a856" to
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a856",
                4,
                "Sunderland Bags",
                AdvertisedStart(now + 1235999),
                "161d9be2-e909-4326-8c2c-35ed71fb460b"
            ),
            "32416b1d-5ff6-4218-b198-5853e576a8a6" to
            RaceSummaryDTO(
                "32416b1d-5ff6-4218-b198-5853e576a8a6",
                2,
                "Sunderland Bags",
                AdvertisedStart(now  + 1000),
                "9daef0d7-bf3c-4f50-921d-8e818c60fe61"
            )
        )

        val apiHelper = mockk<ApiHelper>()
        val raceListViewModel = RaceListViewModel(apiHelper)
        val result = raceListViewModel.process(
            map,
            RaceCategory.entries
        )
        Assert.assertEquals(
            mapOf(
                "GREYHOUND_RACING" to listOf(
                    RaceSummaryDTO(
                        "32416b1d-5ff6-4218-b198-5853e576a8a6",
                        2,
                        "Sunderland Bags",
                        AdvertisedStart(now  + 1000),
                        "9daef0d7-bf3c-4f50-921d-8e818c60fe61",
                        countDown = TimeUtils.showCountDownTime(System.currentTimeMillis() / 1000, now + 1000)
                    )
                ),
                "HARNESS_RACING" to listOf(
                    RaceSummaryDTO(
                        "32416b1d-5ff6-4218-b198-5853e576a856",
                        4,
                        "Sunderland Bags",
                        AdvertisedStart(now + 1235999),
                        "161d9be2-e909-4326-8c2c-35ed71fb460b",
                        countDown = TimeUtils.showCountDownTime(System.currentTimeMillis() / 1000, now + 1235999)
                    )
                ),
                "HORSE_RACING" to emptyList<RaceSummaryDTO>(),
                "ALL" to listOf(
                    RaceSummaryDTO(
                        "32416b1d-5ff6-4218-b198-5853e576a8a6",
                        2,
                        "Sunderland Bags",
                        AdvertisedStart(now  + 1000),
                        "9daef0d7-bf3c-4f50-921d-8e818c60fe61",
                        countDown = TimeUtils.showCountDownTime(System.currentTimeMillis() / 1000, now + 1000)
                    ),
                    RaceSummaryDTO(
                        "32416b1d-5ff6-4218-b198-5853e576a856",
                        4,
                        "Sunderland Bags",
                        AdvertisedStart(now + 1235999),
                        "161d9be2-e909-4326-8c2c-35ed71fb460b",
                        countDown = TimeUtils.showCountDownTime(System.currentTimeMillis() / 1000, now + 1235999)
                    )
                )
            ).toString(),
            result.toString()
        )
    }
}