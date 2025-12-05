package com.example.f1application.features.home.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.Race
import com.example.f1application.features.home.viewModel.HomeUiState
import com.example.f1application.features.home.viewModel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val state = uiState // ← фиксируем значение для smart cast
    when (state) {
        is HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: ${state.message}")
            }
        }

        is HomeUiState.Success -> {
            LazyColumn {
                item {
                    NextRaceCard(race = state.nextRace) {
                        viewModel.onRaceClick(state.nextRace)

                    }
                }
                // TODO: добавить топ-3 пилотов и команд
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NextRaceCard(race: Race, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Название гонки
            Text(
                text = race.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Страна + трасса
            Text(text = "${race.circuit.country} • ${race.circuit.name}")

            Spacer(modifier = Modifier.height(8.dp))

            // Дата
            Text(text = "Дата проведения: ${race.date}")

            Spacer(modifier = Modifier.height(8.dp))

            // Фото трассы
            if (!race.circuit.imageUrl.isNullOrBlank()) {
                GlideImage(
                    model = race.circuit.imageUrl,
                    contentDescription = "Трасса ${race.circuit.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

//@Composable
//fun TopDriversSection(drivers: List<Driver>, navController: NavController) {
//    LazyRow { items(drivers) { driver -> DriverCard(driver) { navController.navigate("driver/${driver.id}") } } }
//}
//
//@Composable
//fun TopTeamsSection(teams: List<Team>) { /* аналогично */ }