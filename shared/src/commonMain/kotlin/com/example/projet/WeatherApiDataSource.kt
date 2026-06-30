package com.example.projet

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherApiDataSource(private val client: HttpClient) {
    suspend fun loadWeather(cityName: String): List<Weather> {
        return client.get("${BuildConfig.WEATHER_BASE_URL}/api/weather?cityname=$cityName")
            .body<List<Weather>>()
            .map { it.copy(icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png") }
    }
}