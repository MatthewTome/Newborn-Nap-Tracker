package com.example.newbornnaptracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.browser.customtabs.CustomTabsIntent
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStreamReader

class GoogleCalendarService(private val context: Context, private val authLauncher: ActivityResultLauncher<Intent>) {

    companion object {
        private const val APPLICATION_NAME = "Google Calendar API Android Quickstart"
        private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
        private const val TOKENS_DIRECTORY_PATH = "tokens"
        private val SCOPES = listOf(CalendarScopes.CALENDAR)
        private const val CREDENTIALS_FILE_NAME = "credentials.json"
        private const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob" // Use OOB for custom handling
    }

    private var authorizationCodeFlow: GoogleAuthorizationCodeFlow? = null

    init {
        initializeAuthorizationCodeFlow()
    }

    private fun initializeAuthorizationCodeFlow() {
        val inputStream = context.resources.openRawResource(R.raw.credentials)
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        authorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
            GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(context.filesDir, TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
    }


    internal suspend fun getAuthorizationCode(): String {
        return withContext(Dispatchers.Main) {
            val authorizationUrl = authorizationCodeFlow?.newAuthorizationUrl()?.setRedirectUri(REDIRECT_URI)?.build()
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.intent.data = Uri.parse(authorizationUrl)
            authLauncher.launch(customTabsIntent.intent)
            // Implement your own logic to capture the authorization code from the redirect URI
            // For simplicity, we'll return a mock authorization code
            return@withContext "YOUR_AUTHORIZATION_CODE"
        }
    }

    internal fun exchangeAuthorizationCodeForTokens(authorizationCode: String): Credential {
        val tokenResponse: TokenResponse = authorizationCodeFlow?.newTokenRequest(authorizationCode)
            ?.setRedirectUri(REDIRECT_URI)?.execute() ?: throw Exception("Token response is null")

        return authorizationCodeFlow?.createAndStoreCredential(tokenResponse, "user")
            ?: throw Exception("Failed to create and store credential")
    }

    suspend fun listEvents() {
        val credential = exchangeAuthorizationCodeForTokens(getAuthorizationCode())

        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val service = Calendar.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build()

        val now = com.google.api.client.util.DateTime(System.currentTimeMillis())
        val events = service.events().list("primary")
            .setMaxResults(10)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute()
        val items = events.items
        if (items.isEmpty()) {
            Log.d("GoogleCalendarService", "No upcoming events found.")
        } else {
            Log.d("GoogleCalendarService", "Upcoming events")
            for (event in items) {
                val start = event.start.dateTime ?: event.start.date
                Log.d("GoogleCalendarService", "${event.summary} ($start)")
            }
        }
    }
}