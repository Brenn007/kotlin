package com.example.projet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.projet.di.appModule
import com.example.projet.screens.SearchScreen
import com.example.projet.theme.AppTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                SearchScreen()
            }
        }
    }
}