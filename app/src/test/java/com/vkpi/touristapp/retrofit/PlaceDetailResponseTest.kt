package com.vkpi.touristapp.retrofit

import com.google.gson.JsonParser
import com.vkpi.touristapp.ResponseFilesTemplates
import com.vkpi.touristapp.mockutils.MockServerUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlaceDetailResponseTest: BaseResponseTests() {
    @Test
    fun `for given place, should return place detail`(){
        MockServerUtil.setMockServerResponse(ResponseFilesTemplates.PLACE_DETAIL_RESPONSE_SUCCESS,server,200)
        val response = runBlocking { service.getPlaceDetailResponse(xid = "R4682064") }
        val json = gson.toJson(response.body())

        val resultResponse = JsonParser().parse(json)
        val expectedResponse =  JsonParser().parse(MockServerUtil.mockedResponse)
        println(resultResponse)
        Assert.assertTrue(resultResponse == expectedResponse)
    }

}