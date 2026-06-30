package com.example.projet

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import com.example.projet.screens.PictureRowItem

@Composable
actual fun WeatherList(items: List<Weather>, modifier: Modifier) {
    LazyRow(modifier = modifier) {
        items(items) { weather ->
            PictureRowItem(data = weather, modifier = Modifier.width(250.dp))
        }
    }
}