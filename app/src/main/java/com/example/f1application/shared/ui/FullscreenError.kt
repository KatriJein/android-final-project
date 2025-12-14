package com.example.f1application.shared.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FullscreenError(
    retry: () -> Unit,
    text: String? = null,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(Dimens.size8),
        contentAlignment = Alignment.Center
    ) {
        ErrorItem(
            error = text,
            onClick = retry,
        )
    }
}

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    error: String? = null,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Dimens.size4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = error ?: "Что-то пошло не так",
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null
        )
    }
}