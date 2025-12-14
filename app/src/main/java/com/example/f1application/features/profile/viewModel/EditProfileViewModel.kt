package com.example.f1application.features.profile.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val repository: ProfileRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repository.observeProfile().collect { profile ->
                _uiState.update {
                    it.copy(
                        name = profile.fullName,
                        photoUrl = profile.photoUri,
                        photoUri = if (profile.photoUri.isNotEmpty()) profile.photoUri.toUri() else Uri.EMPTY
                    )
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onPhotoClick() {
        _uiState.update { it.copy(showImageSourceDialog = true) }
    }

    fun onPhotoSelected(uri: Uri) {
        _uiState.update {
            it.copy(
                photoUri = uri,
                photoUrl = uri.toString(),
                showImageSourceDialog = false
            )
        }
    }

    fun setTempCameraUri(uri: Uri) {
        _uiState.update { it.copy(tempCameraUri = uri) }
    }

    fun dismissImageSourceDialog() {
        _uiState.update { it.copy(showImageSourceDialog = false) }
    }

    fun dismissPermissionDeniedDialog() {
        _uiState.update { it.copy(showPermissionDeniedDialog = false) }
    }

    fun showPermissionDeniedDialog() {
        _uiState.update { it.copy(showPermissionDeniedDialog = true) }
    }

    fun onSaveClick() {
        val state = _uiState.value
        if (state.name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.setProfile(
                    fullName = state.name,
                    photoUri = state.photoUrl
                )
                topLevelBackStack.removeLast()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Ошибка")
                }
            }
        }
    }

    fun onBackClick() {
        topLevelBackStack.removeLast()
    }

    fun createTempCameraUri(): Uri {
        val tempFile = File.createTempFile(
            "profile_photo_${System.currentTimeMillis()}",
            ".jpg",
            context.cacheDir
        )
        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
    }

    fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}

data class EditProfileState(
    val name: String = "",
    val photoUrl: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showImageSourceDialog: Boolean = false,
    val showPermissionDeniedDialog: Boolean = false,
    val tempCameraUri: Uri? = null
)