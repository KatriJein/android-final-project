package com.example.f1application.features.drivers.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.DriverResult
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.features.drivers.viewModel.DriverDetailUiState
import com.example.f1application.features.drivers.viewModel.DriverDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun DriverDetailScreen(driverId: String) {
    val viewModel: DriverDetailViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(driverId) {
        viewModel.loadDriverDetails(driverId)
    }

    val state = uiState
    when (state) {
        is DriverDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DriverDetailUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Ошибка: ${state.message}")
            }
        }
        is DriverDetailUiState.Success -> {
            DriverDetailContent(standing = state.driverStanding, results = state.results)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriverDetailContent(standing: DriverStanding, results: List<DriverResult>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // --- Основная информация ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!standing.driver.imageUrl.isNullOrBlank()) {
                        GlideImage(
                            model = standing.driver.imageUrl,
                            contentDescription = "${standing.driver.firstName} ${standing.driver.lastName}",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "${standing.driver.firstName} ${standing.driver.lastName}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = standing.team.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Место: #${standing.position}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Баллы: ${standing.points}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Победы: ${standing.wins}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // --- Результаты гонок ---
        item {
            Text(
                text = "Результаты гонок",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(results) { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = result.race.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Позиция: ${result.result.finishingPosition}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Старт: ${result.result.gridPosition}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Время: ${result.result.raceTime}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Баллы: ${result.result.pointsObtained}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}