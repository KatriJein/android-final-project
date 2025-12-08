package com.example.f1application.features.drivers.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.model.DriverStanding
import com.example.f1application.features.drivers.viewModel.DriversListUiState
import com.example.f1application.features.drivers.viewModel.DriversListViewModel
import com.example.f1application.shared.ui.CustomHeader
import com.example.f1application.shared.ui.Dimens
import com.example.f1application.shared.ui.FullscreenError
import com.example.f1application.shared.ui.FullscreenLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DriversListScreen() {
    val viewModel = koinViewModel<DriversListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isAscending by viewModel.isAscending.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(isAscending) {
        lazyListState.animateScrollToItem(0)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomHeader(
            title = "Пилоты 2025",
            sortAction = { viewModel.toggleSortOrder() },
            isAscending = isAscending
        )

        val state = uiState
        when (state) {
            is DriversListUiState.Loading -> {
                FullscreenLoading()
            }

            is DriversListUiState.Error -> {
                FullscreenError(
                    retry = { viewModel.retry() },
                    text = state.message,
                )
            }

            is DriversListUiState.Success -> {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(state.standings) { standing ->
                        DriverStandingItemFull(standing = standing) {
                            viewModel.onDriverClick(standing)
                        }
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
            .padding(Dimens.Small)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = standing.driver.imageUrl,
            contentDescription = "${standing.driver.firstName} ${standing.driver.lastName}",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(Dimens.Large)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.Medium
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
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}