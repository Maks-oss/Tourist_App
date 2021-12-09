package com.vkpi.touristapp.mockutils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.InputStreamReader

private object MockResponseFileReader{
    fun readFile(path:String):String{
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        val content = reader.readText()
        reader.close()
        return content
    }
}

object MockServerUtil  {
    var mockedResponse = ""
    fun setMockServerResponse(path:String,mockWebServer: MockWebServer,status:Int){
        mockedResponse =
            MockResponseFileReader.readFile(path)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(status)
                .setBody(mockedResponse)
        )
    }
}