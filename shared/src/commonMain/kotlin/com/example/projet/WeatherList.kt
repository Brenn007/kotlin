package com.example.projet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.projet.screens.PictureRowItem

@Composable
expect fun WeatherList(items: List<Weather>, modifier: Modifier = Modifier)