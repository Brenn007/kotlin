package com.example.projet

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertTrue

class MainViewModelTest {

    @Test
    fun testLoadWeather() = runBlocking {
        assertTrue(
            BuildConfig.WEATHER_API_KEY.isNotEmpty(),
            "WEATHER_API_KEY doit être défini (local.properties ou secret GitHub)"
        )
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        val api = WeatherApiDataSource(client)
        val weathers = api.loadWeather("toulouse")
        assertTrue(weathers.isNotEmpty(), "La liste de météo ne doit pas être vide")
        client.close()
    }
}
