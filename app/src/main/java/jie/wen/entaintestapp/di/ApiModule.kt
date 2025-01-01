package jie.wen.entaintestapp.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jie.wen.entaintestapp.api.ApiHelper
import jie.wen.entaintestapp.api.ApiHelperImpl
import jie.wen.entaintestapp.api.ApiService
import jie.wen.entaintestapp.data.Constants.Companion.API_CALL_TIMEOUT
import jie.wen.entaintestapp.data.Constants.Companion.BASE_URL
import jie.wen.entaintestapp.data.Constants.Companion.RETRY_TIMES
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideBaseURL() = BASE_URL

    // for debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    class RetryInterceptor(private val retryAttempts: Int) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            for (i in 1..retryAttempts) {
                try {
                    return chain.proceed(chain.request())
                } catch (e: SocketTimeoutException) {
                   Log.e("Fail to connect", e.message.toString())
                }
            }
            throw RuntimeException("failed to compile the request")
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(API_CALL_TIMEOUT, TimeUnit.MILLISECONDS)
        .connectTimeout(API_CALL_TIMEOUT, TimeUnit.MILLISECONDS)
        // for debug
//        .addInterceptor(loggingInterceptor)
        .addInterceptor(RetryInterceptor(RETRY_TIMES))
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}