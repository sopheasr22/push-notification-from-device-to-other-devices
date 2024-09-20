package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {
    val user1 = "30731cf2-4fd0-4a84-8074-d0cbdb7db167"
    val user2 = "9fd3accd-403d-431b-afa0-297310af568c"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
SendNotification(targetPlayerId = listOf(user2))

        }
    }

    @Composable
    fun SendNotification(targetPlayerId: List<String>) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    sendPushNotification(
                        targetPlayerId,
                        "Hello Friend",
                        "This is a test notification"
                    )
                }
            ) {
                Text(text = "Send Notification to User 2")
            }
        }
    }


    fun sendPushNotification(
        subscriptionIds: List<String>,
        title: String,
        message: String
    ) {

        val appId = "7d99c932-eb2a-4daa-9def-dac213ce7fd0"
        val restApiKey = "YzZhMjJlNjctNGU1ZC00M2RmLTk2MDAtMzc1OGNhZDM2OTVj"

        // Create OkHttpClient
        val client = OkHttpClient()

        // Create the JSON request body
        val jsonBody = JSONObject().apply {
            put("app_id", appId)
            put("headings", JSONObject().put("en", title))
            put("contents", JSONObject().put("en", message))

            // Convert the List<String> into a JSONArray
            put("include_subscription_ids", JSONArray(subscriptionIds))
        }


        val body = jsonBody.toString().toRequestBody(
            "application/json; charset=utf-8".toMediaTypeOrNull()
        )

        // Build the request
        val request = Request.Builder()
            .url("https://api.onesignal.com/notifications")
            .post(body)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Authorization", "Basic $restApiKey") // Add your OneSignal REST API key
            .build()
        Log.d("Request notification", "Request: $request")


        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Response notification", "Failed to send notification: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d(
                        "Response notification",
                        "Notification sent successfully: ${response.body?.string()}"
                    )
                } else {
                    Log.d(
                        "Response notification",
                        "Failed to send notification. Error: ${response.code} - ${response.body?.string()}"
                    )
                }
            }
        })
    }
}
