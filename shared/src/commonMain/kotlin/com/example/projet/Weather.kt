package com.example.projet

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val id: Int,
    val name: String,
    val temp: Double,
    val speed: Double,
    val description: String,
    val icon: String
) {
    fun getResume(): String {
        val cityName = name.replaceFirstChar { it.uppercase() }
        return "Il fait $temp° à $cityName (id=$id) avec un vent de $speed m/s\n" +
               "-Description : $description\n" +
               "-Icône : $icon"
    }
}
