package com.example.f1application.features.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.core.db.FavoriteDriverEntity
import com.example.f1application.core.model.ProfileEntity
import com.example.f1application.features.profile.viewModel.ProfileViewModel
import com.example.f1application.features.profile.viewModel.ProfileViewState
import com.example.f1application.shared.ui.CustomHeader
import com.example.f1application.shared.ui.Dimens
import com.example.f1application.shared.ui.FullscreenError
import com.example.f1application.shared.ui.FullscreenLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CustomHeader(
                title = "Профиль"
            )
        }, contentWindowInsets = WindowInsets(0)
    ) { padding ->
        when (val state = uiState) {
            is ProfileViewState.Loading -> {
                FullscreenLoading()
            }

            is ProfileViewState.Error -> {
                FullscreenError(retry = { viewModel.retry() })
            }

            is ProfileViewState.Success -> {
                ProfileContent(
                    state = state,
                    onEditClick = viewModel::onEditProfile,
                    onDriverClick = viewModel::onDriverClick,
                    onSearchDrivers = viewModel::onSearchDrivers,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProfileContent(
    state: ProfileViewState.Success,
    onEditClick: () -> Unit,
    onDriverClick: (String) -> Unit,
    onSearchDrivers: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.size8)
    ) {
        item {
            ProfileHeader(
                profile = state.profile,
                onEditClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.size16)
                    .padding(bottom = Dimens.size8)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = true
                    )
            )
        }

        item {
            CustomHeader(
                title = "Избранные пилоты"
            )
        }

        if (state.favouritesDrivers.isEmpty()) {
            item {
                EmptyFavoritesSection(
                    modifier = Modifier.padding(horizontal = Dimens.size16),
                    onSearchDrivers = onSearchDrivers
                )
            }
        } else {
            items(
                items = state.favouritesDrivers,
                key = { it.driverId }
            ) { driver ->
                FavoriteDriverCard(
                    driver = driver,
                    onClick = { onDriverClick(driver.driverId) },
                    modifier = Modifier.padding(horizontal = Dimens.size16)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProfileHeader(
    profile: ProfileEntity,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(140.dp),
                    color = Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(
                        3.dp,
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
                ) {}

                if (profile.photoUri.isNotBlank()) {
                    GlideImage(
                        model = profile.photoUri,
                        contentDescription = "Фото профиля",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Фото по умолчанию",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(128.dp)
                    )
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать профиль",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = profile.fullName.ifEmpty { "Гость" },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FavoriteDriverCard(
    driver: FavoriteDriverEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.size4)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = driver.imageUrl,
            contentDescription = "${driver.firstName} ${driver.lastName}",
            modifier = Modifier
                .size(Dimens.size86)
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
                    text = "${driver.firstName} ${driver.lastName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            driver.teamName?.let {
                Text(
                    text = it, style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesSection(
    modifier: Modifier = Modifier,
    onSearchDrivers: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.size16),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Нет избранных",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(Dimens.size16))
            Text(
                text = "Пока нет избранных пилотов",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { onSearchDrivers() },
                modifier = Modifier.padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Dimens.size12)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.size8)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить",
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Перейти к списку пилотов")
                }
            }
        }
    }
}
