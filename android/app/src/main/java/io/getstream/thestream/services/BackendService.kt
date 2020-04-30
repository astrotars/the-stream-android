package io.getstream.thestream.services

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object BackendService {
    private val http = OkHttpClient()
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private const val apiRoot = "https://63119ec9.ngrok.io" // or use ngrok

    private lateinit var authToken: String

    fun signIn(user: String) {
        authToken = post(
            "/v1/users",
            mapOf("user" to user)
        )
            .getString("authToken")
    }

    data class FeedCredentials(val token: String, val apiKey: String)

    fun getFeedCredentials(): FeedCredentials {
        val response = post(
            "/v1/stream-feed-credentials",
            mapOf(),
            authToken
        )

        return FeedCredentials(
            response.getString("token"),
            response.getString("apiKey")
        )
    }

    fun getUsers(): List<String> {
        val request = Request.Builder()
            .url("$apiRoot/v1/users")
            .addHeader("Authorization", "Bearer $authToken")
            .get()

        http.newCall(request.build()).execute().use {
            val jsonArray = JSONArray(it.body!!.string())
            return List(jsonArray.length()) { i -> jsonArray.get(i).toString() }
        }
    }

    private fun post(path: String, body: Map<String, Any>, authToken: String? = null): JSONObject {
        val request = Request.Builder()
            .url("$apiRoot${path}")
            .post(JSONObject(body).toString().toRequestBody(JSON))

        if (authToken != null) {
            request.addHeader("Authorization", "Bearer $authToken")
        }

        http.newCall(request.build()).execute().use {
            return JSONObject(it.body!!.string())
        }
    }
}