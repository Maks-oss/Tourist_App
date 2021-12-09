package com.vkpi.touristapp.retrofit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.GsonBuilder
import com.vkpi.touristapp.mockutils.MockServerUtil
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseResponseTests {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    protected val server = MockWebServer()
    protected lateinit var service: PlaceService
    protected val gson = GsonBuilder()
        .setLenient()
        .create()

    @Before
    fun init() {
        server.start(8000)
        val BASE_URL = server.url("/test_url/").toString()
        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create(PlaceService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
        MockServerUtil.mockedResponse=""
    }
}