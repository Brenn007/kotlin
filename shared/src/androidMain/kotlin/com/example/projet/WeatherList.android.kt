package com.example.projet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.projet.screens.PictureRowItem

@Composable
actual fun WeatherList(items: List<Weather>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(items) { weather ->
            PictureRowItem(data = weather, modifier = Modifier.fillMaxWidth())
        }
    }
}