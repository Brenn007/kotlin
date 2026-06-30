@file:Suppress("SpellCheckingInspection")

package com.example.projet.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.projet.WeatherList
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.projet.MainViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.example.projet.Weather
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import projet.shared.generated.resources.Res
import projet.shared.generated.resources.clear_filter
import projet.shared.generated.resources.error
import projet.shared.generated.resources.load_data

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = koinViewModel()) {
    val list by viewModel.dataList.collectAsStateWithLifecycle()
    val runInProgress by viewModel.runInProgress.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val searchText: MutableState<String> = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadWeathers("toulouse")
    }

    val filteredList = list?.filter { weather ->
        weather.name.contains(searchText.value, ignoreCase = true)
    } ?: emptyList()

    Column(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        SearchBar(modifier = Modifier.fillMaxWidth(), searchText = searchText)

        MyError(message = errorMessage)

        AnimatedVisibility(visible = runInProgress) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.padding(8.dp))
            }
        }

        WeatherList(items = filteredList, modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                searchText.value = ""
                viewModel.resetData()
            }) {
                Text(stringResource(Res.string.clear_filter))
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                viewModel.loadWeathers(searchText.value)
            }) {
                Text(stringResource(Res.string.load_data))
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, searchText: MutableState<String>) {
    TextField(
        value = searchText.value,
        onValueChange = { searchText.value = it },
        placeholder = { Text("Votre recherche ici") },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier
    )
}

@Composable
fun PictureRowItem(modifier: Modifier = Modifier, data: Weather) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = data.icon,
            contentDescription = data.name,
            contentScale = ContentScale.Fit,
            error = painterResource(Res.drawable.error),
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .clickable { expanded = !expanded }
                .animateContentSize()
        ) {
            Text(text = data.name, fontSize = 20.sp)
            Text(
                text = if (expanded) data.getResume() else data.getResume().take(20),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}