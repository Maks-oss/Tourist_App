package com.vkpi.touristapp.retrofit

import com.google.gson.JsonParser
import com.vkpi.touristapp.retrofit.BaseResponseTests
import com.vkpi.touristapp.ResponseFilesTemplates
import com.vkpi.touristapp.mockutils.MockServerUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlacesResponseTest : BaseResponseTests() {
    @Test
    fun `if place with geo parameters exists, response should be successful`() {
        MockServerUtil.setMockServerResponse(ResponseFilesTemplates.PLACE_RESPONSE_SUCCESS,server,200)
        val response = runBlocking { service.getPlacesResponse(name = "1000",lat = "51.50853",lon = "-0.12574") }
        val json = gson.toJson(response.body())

        val resultResponse = JsonParser().parse(json)
        val expectedResponse =  JsonParser().parse(MockServerUtil.mockedResponse)

        Assert.assertTrue(resultResponse == expectedResponse)
    }
    @Test
    fun `if place with geo parameters does not exists response should be empty`() {
        MockServerUtil.setMockServerResponse(ResponseFilesTemplates.PLACE_RESPONSE_EMPTY,server,200)
        val response = runBlocking { service.getPlacesResponse(name = "1000",lat = "-0.12574",lon = "-0.12574") }
        val json = gson.toJson(response.body())

        val resultResponse = JsonParser().parse(json)
        val expectedResponse =  JsonParser().parse(MockServerUtil.mockedResponse)

        Assert.assertTrue(resultResponse == expectedResponse)
    }
}