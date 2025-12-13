package com.example.f1application.features.drivers.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.DriverResult
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.features.drivers.viewModel.DriverDetailUiState
import com.example.f1application.features.drivers.viewModel.DriverDetailViewModel
import com.example.f1application.shared.CountryCodes
import com.example.f1application.shared.ui.Dimens
import com.example.f1application.shared.ui.FullscreenError
import com.example.f1application.shared.ui.FullscreenLoading
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun DriverDetailScreen(driverId: String) {
    val viewModel: DriverDetailViewModel = koinViewModel {
        parametersOf(driverId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.toggleFavorite() },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное"
                )
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        when (val state = uiState) {
            is DriverDetailUiState.Loading -> FullscreenLoading()
            is DriverDetailUiState.Error -> FullscreenError(
                retry = { viewModel.retry() },
                text = state.message
            )

            is DriverDetailUiState.Success -> {
                DriverDetailsWithResultsContent(
                    standing = state.driverStanding,
                    results = state.results,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriverDetailsWithResultsContent(
    standing: DriverStanding,
    results: List<DriverResult>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            DriverHeader(standing)
        }

        item {
            DriverStats(standing)
        }

        item {
            PersonalInfo(standing)
        }

        item {
            TeamInfo(standing)
        }

        if (results.isNotEmpty()) {
            item {
                Text(
                    text = "Результаты гонок",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = Dimens.Large)
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }

            items(results, key = { it.race.id }) { result ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = Dimens.Large)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RaceResultItem(
                        result = result,
                        isSprint = false
                    )

                    result.sprintResult?.let { sprintResult ->
                        RaceResultItem(
                            result = DriverResult(
                                race = result.race,
                                result = com.example.f1application.core.model.RaceResult(
                                    finishingPosition = sprintResult.finishingPosition ?: "N/A",
                                    gridPosition = sprintResult.gridPosition.toString(),
                                    raceTime = sprintResult.raceTime ?: "",
                                    pointsObtained = sprintResult.pointsObtained,
                                    retired = sprintResult.retired
                                ),
                                sprintResult = null
                            ),
                            isSprint = true
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriverHeader(standing: DriverStanding) {
    Column {
        GlideImage(
            model = standing.driver.imageUrl,
            contentDescription = "${standing.driver.firstName} ${standing.driver.lastName}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(bottomStart = Dimens.Large, bottomEnd = Dimens.Large)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${standing.driver.firstName} ${standing.driver.lastName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${standing.driver.number}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = standing.team.color
                )
            }
        }
    }

}

@Composable
fun DriverStats(standing: DriverStanding) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.ScreenPadding),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(icon = Icons.Default.Star, label = "Очки", value = standing.points)
            StatItem(
                icon = Icons.Default.EmojiEvents,
                label = "Позиция",
                value = standing.position
            )
            StatItem(
                icon = Icons.Default.MilitaryTech,
                label = "Победы",
                value = standing.wins
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonalInfo(standing: DriverStanding) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                standing.driver.country.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Medium))

                val flagUrl = CountryCodes.getFlagUrl(standing.driver.country)
                GlideImage(
                    model = flagUrl,
                    contentDescription = "Флаг ${standing.driver.country}",
                    modifier = Modifier
                        .size(32.dp)

                )
            }

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val age = standing.driver.birthday.let { birthdayString ->
                try {
                    val birthday = LocalDate.parse(birthdayString, formatter)
                    Period.between(birthday, LocalDate.now()).years
                } catch (e: Exception) {
                    null
                }
            }
            Text(
                text = buildAnnotatedString {
                    append("Дата рождения: ${standing.driver.birthday}")
                    age?.let {
                        append(" (${it} ${getAgeSuffix(it)})")
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun getAgeSuffix(age: Int): String {
    return when {
        age % 10 == 1 && age % 100 != 11 -> "год"
        age % 10 in 2..4 && age % 100 !in 12..14 -> "года"
        else -> "лет"
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TeamInfo(standing: DriverStanding) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            standing.team.name.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                )
            }

            GlideImage(
                model = standing.team.imageUrl,
                contentDescription = standing.team.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Fit
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                standing.team.country.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.Medium))

                val flagUrl = CountryCodes.getFlagUrl(standing.team.country)
                GlideImage(
                    model = flagUrl,
                    contentDescription = "Флаг ${standing.team.country}",
                    modifier = Modifier
                        .size(32.dp)

                )
            }
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, value: Int?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "$value",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun RaceResultItem(
    result: DriverResult,
    isSprint: Boolean = false
) {
    val finishingPosition = result.result.finishingPosition.toIntOrNull() ?: 0

    val accentColor = if (isSprint) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSprint) 2.dp else 4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = true
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (finishingPosition) {
                    1 -> Color(0xFFFFD700).copy(alpha = if (isSprint) 0.08f else 0.1f)
                    2 -> Color(0xFFC0C0C0).copy(alpha = if (isSprint) 0.08f else 0.1f)
                    3 -> Color(0xFFCD7F32).copy(alpha = if (isSprint) 0.08f else 0.1f)
                    else -> accentColor.copy(alpha = if (isSprint) 0.05f else 0.1f)
                },
                border = BorderStroke(
                    1.dp,
                    when (finishingPosition) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> accentColor.copy(alpha = if (isSprint) 0.3f else 0.5f)
                    }
                )
            ) {
                Text(
                    text = if (result.result.finishingPosition == "NC") {
                        "NC"
                    } else {
                        "P${result.result.finishingPosition}"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = when (finishingPosition) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> accentColor
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = result.race.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (isSprint) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = accentColor.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "Sprint",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Старт: ${result.result.gridPosition}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (result.result.pointsObtained > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${result.result.pointsObtained} pts",
                                style = MaterialTheme.typography.bodySmall,
                                color = accentColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            result.result.raceTime.takeIf { it.isNotBlank() }?.let { time ->
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
