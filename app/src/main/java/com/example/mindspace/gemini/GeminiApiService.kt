package com.example.mindspace.gemini

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService {
    @POST("models/gemini-1.5-flash-latest:generateContent?key=YOUR_API_KEY")
    fun generateContent(@Body request: com.example.mindspace.gemini.RequestBody): Call<ResponseBody>
}

data class RequestBody(val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)

data class ResponseBody(val data: Data)
data class Data(val contents: List<ContentResponse>)
data class ContentResponse(val parts: List<PartResponse>)
data class PartResponse(val text: String)



