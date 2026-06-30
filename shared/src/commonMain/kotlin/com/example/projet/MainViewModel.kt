package com.example.projet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val api: WeatherApiDataSource) : ViewModel() {

    private val _dataList = MutableStateFlow<List<Weather>?>(null)
    val dataList: StateFlow<List<Weather>?> = _dataList.asStateFlow()

    private val _runInProgress = MutableStateFlow(false)
    val runInProgress: StateFlow<Boolean> = _runInProgress.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadFakeData() {
        _dataList.value = listOf(
            Weather(1, "toulouse", 22.5, 3.2, "ciel dégagé", "01d"),
            Weather(2, "paris", 18.0, 5.1, "nuageux", "03d"),
            Weather(3, "marseille", 26.3, 4.8, "ensoleillé", "01d"),
            Weather(4, "bordeaux", 20.1, 6.0, "partiellement nuageux", "02d"),
            Weather(5, "lyon", 19.8, 4.3, "pluie légère", "10d"),
        )
    }

    fun resetData() {
        _dataList.value = null
        _errorMessage.value = null
    }

    fun loadWeathers(cityName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _runInProgress.value = true
            _errorMessage.value = null
            try {
                if (cityName.isNullOrBlank() || cityName.length < 3) {
                    throw IllegalArgumentException("Le nom de la ville doit faire au moins 3 caractères")
                }
                _dataList.value = api.loadWeather(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = e.message
            } finally {
                _runInProgress.value = false
            }
        }
    }
}