package com.vkpi.touristapp.retrofit

import com.google.gson.JsonParser
import com.vkpi.touristapp.ResponseFilesTemplates
import com.vkpi.touristapp.mockutils.MockServerUtil
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityResponseTest: BaseResponseTests() {

    @Test
    fun `if city exists response should be successful`() {
        MockServerUtil.setMockServerResponse(ResponseFilesTemplates.CITY_RESPONSE_SUCCESS,server,200)
        val response = runBlocking { service.getCityResponse(name = "london") }
        val json = gson.toJson(response.body())

        val resultResponse = JsonParser().parse(json)
        val expectedResponse =  JsonParser().parse(MockServerUtil.mockedResponse)

        assertTrue(resultResponse == expectedResponse)
    }
    @Test
    fun `if city does not exists response should be empty`() {
        MockServerUtil.setMockServerResponse(ResponseFilesTemplates.CITY_RESPONSE_ERROR,server,200)
        val response = runBlocking { service.getCityResponse(name = "qqqqqqqqq") }
        val json = gson.toJson(response.body())

        val resultResponse = JsonParser().parse(json)
        val expectedResponse =  JsonParser().parse(MockServerUtil.mockedResponse)

        assertTrue(resultResponse == expectedResponse)
    }

}