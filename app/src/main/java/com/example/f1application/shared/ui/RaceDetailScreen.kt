package com.example.f1application.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.Race
import com.example.f1application.core.model.RaceStatus
import com.example.f1application.shared.CountryCodes

@Composable
fun RaceDetailScreen(
    race: Race
) {
    RaceDetailContent(race)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RaceDetailContent(race: Race) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // === Основная информация ===
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Название гонки
                    Text(
                        text = race.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Раунд
                    Text(
                        text = "Раунд: ${race.round}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Дата
                    Text(
                        text = "Дата: ${race.date}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Статус
                    val statusText = when (race.status) {
                        RaceStatus.UPCOMING -> "Предстоит"
                        RaceStatus.IN_PROGRESS -> "В процессе"
                        RaceStatus.FINISHED -> "Завершена"
                    }
                    Text(
                        text = "Статус: $statusText",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Картинка трассы
                    if (!race.circuit.imageUrl.isNullOrBlank()) {
                        GlideImage(
                            model = race.circuit.imageUrl,
                            contentDescription = "Трасса ${race.circuit.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Информация о трассе
                    Text(
                        text = "Трасса: ${race.circuit.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "${race.circuit.country}, ${race.circuit.city}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Флаг страны
                    val flagUrl = CountryCodes.getFlagUrl(race.circuit.country)
                    if (flagUrl != null) {
                        GlideImage(
                            model = flagUrl,
                            contentDescription = "Флаг ${race.circuit.country}",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Длина: ${race.circuit.lengthKm} км",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Повороты: ${race.circuit.corners}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // === Победитель гонки ===
        if (race.winner != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Победитель гонки",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Фото пилота
                            if (!race.winner.imageUrl.isNullOrBlank()) {
                                GlideImage(
                                    model = race.winner.imageUrl,
                                    contentDescription = "Фото ${race.winner.firstName} ${race.winner.lastName}",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "${race.winner.firstName} ${race.winner.lastName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
//                                Text(
//                                    text = race.winner.team?.name ?: "Команда неизвестна",
//                                    style = MaterialTheme.typography.bodyMedium
//                                )
                                Text(
                                    text = "Номер: ${race.winner.number ?: "—"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Флаг страны
                        val flagUrl = CountryCodes.getFlagUrl(race.winner.country)
                        if (flagUrl != null) {
                            GlideImage(
                                model = flagUrl,
                                contentDescription = "Флаг ${race.winner.country}",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        // === Победитель команды ===
        if (race.teamWinner != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Победитель команды",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Логотип команды
                            if (!race.teamWinner.imageUrl.isNullOrBlank()) {
                                GlideImage(
                                    model = race.teamWinner.imageUrl,
                                    contentDescription = "Логотип ${race.teamWinner.name}",
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = race.teamWinner.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Страна: ${race.teamWinner.country}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
//                                Text(
//                                    text = "Первое участие: ${race.teamWinner.firstAppearance}",
//                                    style = MaterialTheme.typography.bodySmall
//                                )
                            }
                        }
                    }
                }
            }
        }


        // === Дополнительная информация ===
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Дополнительно",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Количество кругов: ${race.laps}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}