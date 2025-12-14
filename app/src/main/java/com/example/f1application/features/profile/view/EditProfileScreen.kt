package com.example.f1application.features.profile.view

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.f1application.features.profile.viewModel.EditProfileViewModel
import com.example.f1application.shared.ui.Dimens
import com.example.f1application.shared.ui.EditHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
    val context = LocalContext.current
    val viewModel: EditProfileViewModel = koinViewModel { parametersOf(context) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoSelected(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            state.tempCameraUri?.let { viewModel.onPhotoSelected(it) }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            state.tempCameraUri?.let { cameraLauncher.launch(it) }
        } else {
            viewModel.showPermissionDeniedDialog()
        }
    }

    fun openCamera() {
        val uri = viewModel.createTempCameraUri()
        viewModel.setTempCameraUri(uri)

        if (viewModel.isCameraPermissionGranted()) {
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EditHeader(
            title = "Редактирование",
            onNavigationClick = { viewModel.onBackClick() },
            actions = {
                IconButton(
                    onClick = { viewModel.onSaveClick() },
                    enabled = state.name.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Сохранить"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.size16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProfileImageWithPicker(
                photoUri = state.photoUri,
                onPhotoClick = viewModel::onPhotoClick
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Имя") },
                placeholder = { Text("Введите ваше имя") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.name.isBlank()
            )

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (state.showImageSourceDialog) {
                ImageSourceDialog(
                    onDismiss = viewModel::dismissImageSourceDialog,
                    onGalleryClick = {
                        galleryLauncher.launch("image/*")
                    },
                    onCameraClick = {
                        viewModel.dismissImageSourceDialog()
                        openCamera()
                    }
                )
            }

            if (state.showPermissionDeniedDialog) {
                PermissionDeniedDialog(
                    onDismiss = viewModel::dismissPermissionDeniedDialog
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImageWithPicker(photoUri: Uri, onPhotoClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable { onPhotoClick() },
        contentAlignment = Alignment.Center
    ) {
        if (photoUri != Uri.EMPTY) {
            GlideImage(
                model = photoUri,
                contentDescription = "Фото профиля",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Фото по умолчанию",
                modifier = Modifier.size(120.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Изменить фото",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ImageSourceDialog(
    onDismiss: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите источник") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.size8)) {
                Button(onClick = onGalleryClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Из галереи")
                }
                Button(onClick = onCameraClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Сделать фото")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
fun PermissionDeniedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Разрешение отклонено") },
        text = { Text("Для использования камеры необходимо разрешение.") },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("ОК") }
        }
    )
}
