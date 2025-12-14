package com.example.f1application.shared.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.Race
import com.example.f1application.core.model.RaceStatus
import com.example.f1application.core.model.Team
import com.example.f1application.core.navigation.DriverDetails
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.shared.CountryCodes
import org.koin.compose.koinInject


@Composable
fun RaceDetailScreen(
    race: Race
) {
    val topLevelBackStack: TopLevelBackStack<Route> = koinInject()
    RaceDetailContent(race, topLevelBackStack)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RaceDetailContent(race: Race, topLevelBackStack: TopLevelBackStack<Route>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(vertical = Dimens.size16)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                if (!race.circuit.imageUrl.isNullOrBlank()) {
                    GlideImage(
                        model = race.circuit.imageUrl,
                        contentDescription = "Трасса ${race.circuit.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFD50000), Color(0xFFB71C1C)
                                    )
                                )
                            )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .align(Alignment.BottomStart)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent, Color.Black.copy(alpha = 0.7f)
                                ), startY = 0f, endY = 150f
                            )
                        )
                )

                Text(
                    text = race.name,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(Dimens.size16)
                        .fillMaxWidth(),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.size16)
                    .shadow(
                        elevation = Dimens.size8, shape = RoundedCornerShape(Dimens.size16), clip = true
                    ), shape = RoundedCornerShape(Dimens.size16), color = Color.White, tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.size8)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,

                        )
                        Text(
                            text = "Информация о гонке",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.size16))

                    InfoGridItem(
                        icon = Icons.Default.Numbers, title = "Этап", value = race.round.toString()
                    )

                    InfoGridItem(
                        icon = Icons.Default.DateRange, title = "Дата", value = race.dateString
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.size8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = null,
                            tint = when (race.status) {
                                RaceStatus.UPCOMING -> Color(0xFF4CAF50) // Зеленый
                                RaceStatus.IN_PROGRESS -> Color(0xFFFF9800) // Оранжевый
                                RaceStatus.FINISHED -> Color(0xFFF44336) // Красный
                            },
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(Dimens.size12))

                        Column {
                            Text(
                                text = "Статус",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = when (race.status) {
                                    RaceStatus.UPCOMING -> "Предстоит"
                                    RaceStatus.IN_PROGRESS -> "В процессе"
                                    RaceStatus.FINISHED -> "Завершена"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    InfoGridItem(
                        icon = Icons.Default.Timeline,
                        title = "Кругов",
                        value = race.laps.toString()
                    )
                }
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.size16)
                    .shadow(
                        elevation = Dimens.size8, shape = RoundedCornerShape(Dimens.size16), clip = true
                    ), shape = RoundedCornerShape(Dimens.size16), color = Color.White, tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.size16)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.size8)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Трасса",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.size16))

                    Text(
                        text = race.circuit.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(Dimens.size8))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.size8)
                    ) {
                        val flagUrl = CountryCodes.getFlagUrl(race.circuit.country)
                        if (flagUrl != null) {
                            Surface(
                                shape = RoundedCornerShape(Dimens.size8),
                                color = Color.Transparent,
                                modifier = Modifier.size(32.dp)
                            ) {
                                GlideImage(
                                    model = flagUrl,
                                    contentDescription = "Флаг ${race.circuit.country}",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(Dimens.size8)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "${race.circuit.country}, ${race.circuit.city}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.size16))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.size8)
                    ) {
                        InfoBadge(
                            icon = Icons.Default.Timeline,
                            text = "${race.circuit.lengthKm} км",
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        InfoBadge(
                            icon = Icons.Default.TurnRight,
                            text = "${race.circuit.corners} поворотов",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }

        if (race.winner != null) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.size16)
                        .shadow(
                            elevation = Dimens.size8, shape = RoundedCornerShape(Dimens.size16), clip = true
                        ),
                    shape = RoundedCornerShape(Dimens.size16),
                    color = Color.White,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.size16)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens.size8)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = "Победитель гонки",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    topLevelBackStack.add(
                                        DriverDetails(
                                            driverId = race.winner.id
                                        )
                                    )
                                }) {
                            if (!race.winner.imageUrl.isNullOrBlank()) {
                                GlideImage(
                                    model = race.winner.imageUrl,
                                    contentDescription = "Фото ${race.winner.firstName} ${race.winner.lastName}",
                                    modifier = Modifier
                                        .size(Dimens.size100)
                                        .clip(RoundedCornerShape(Dimens.size16)),
                                    contentScale = ContentScale.Crop
                                )

                            } else {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    modifier = Modifier.size(Dimens.size100)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(Dimens.size16))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Dimens.size4)
                                ) {
                                    Text(
                                        text = "${race.winner.firstName} ${race.winner.lastName}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "${race.winner.number}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = race.teamWinner?.color ?: Color.Black,
                                        modifier = Modifier.padding(
                                            horizontal = Dimens.size12
                                        )
                                    )
                                }

                            }
                        }

                        if (race.teamWinner != null) {
                            TeamInfoSection(team = race.teamWinner)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun TeamInfoSection(team: Team) {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.size16),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(2.dp)
        ) {
            Box(modifier = Modifier.height(2.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.size16)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Команда",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(Dimens.size4))

                Text(
                    text = team.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (!team.imageUrl.isNullOrBlank()) {

                Surface(
                    shape = RoundedCornerShape(Dimens.size12),
                    color = Color.Transparent,
                    modifier = Modifier
                        .height(60.dp)
                        .width(120.dp)
                ) {
                    GlideImage(
                        model = team.imageUrl,
                        contentDescription = "Логотип ${team.name}",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.size12),
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(Dimens.size12),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier
                        .height(60.dp)
                        .width(120.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoGridItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(Dimens.size12))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun InfoBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.size8),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.size4)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(Dimens.size16)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}