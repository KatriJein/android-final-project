package com.example.f1application.features.home.view

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.ConstructorStanding
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.features.home.viewModel.HomeUiState
import com.example.f1application.features.home.viewModel.HomeViewModel
import com.example.f1application.shared.ui.CustomHeader
import com.example.f1application.shared.ui.Dimens
import com.example.f1application.shared.ui.FullscreenError
import com.example.f1application.shared.ui.FullscreenLoading
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        CustomHeader(
            title = "Главная"
        )

        val state = uiState
        when (state) {
            is HomeUiState.Loading -> {
                FullscreenLoading()
            }

            is HomeUiState.Error -> {
                FullscreenError(
                    retry = { viewModel.retry() },
                    text = state.message,
                )
            }

            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.size16),
                    contentPadding = PaddingValues(Dimens.size16)
                ) {
                    item {
                        Text(
                            text = "Лидеры чемпионата",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = Dimens.size8)
                        )
                    }

                    items(state.topDrivers) { standing ->
                        DriverStandingItemFull(standing = standing) {
                            viewModel.onDriverClick(standing.driver.id)
                        }
                    }

                    item {
                        Text(
                            text = "Конструкторский зачёт",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = Dimens.size16, bottom = Dimens.size8)
                        )
                    }

                    items(state.topTeams) { standing ->
                        TeamListItemFull(standing = standing)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriverStandingItemFull(standing: DriverStanding, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.size4)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = standing.driver.imageUrl,
            contentDescription = "${standing.driver.firstName} ${standing.driver.lastName}",
            modifier = Modifier
                .size(Dimens.size100)
                .clip(RoundedCornerShape(Dimens.size16)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(Dimens.size12))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.size4)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.size8
                )
            ) {
                Text(
                    text = "${standing.driver.firstName} ${standing.driver.lastName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = standing.driver.number.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = standing.team.color
                )
            }
            Text(
                text = standing.team.name, style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = standing.points.toString(),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(end = Dimens.size8)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TeamListItemFull(standing: ConstructorStanding) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.size4)
            .shadow(
                elevation = Dimens.size4,
                shape = RoundedCornerShape(Dimens.size12),
                clip = true
            ),
        shape = RoundedCornerShape(Dimens.size12),
        color = Color.White,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!standing.team.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    GlideImage(
                        model = standing.team.imageUrl,
                        contentDescription = standing.team.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(topStart = Dimens.size12, topEnd = Dimens.size12)),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.size16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = standing.team.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${standing.points} очков",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
        }
    }
}