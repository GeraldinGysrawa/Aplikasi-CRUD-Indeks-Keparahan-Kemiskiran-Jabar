package com.example.hanyarunrun.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hanyarunrun.data.AppDatabase
import com.example.hanyarunrun.data.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).profileDao()
    val dataList: LiveData<List<ProfileEntity>> = dao.getAll()

    private val _profileStateFlow = MutableStateFlow<ProfileEntity?>(null)
    val profileStateFlow: StateFlow<ProfileEntity?> = _profileStateFlow

    init {
        loadProfile()
    }

    fun insertData(username: String, uid: String, email: String) {
        viewModelScope.launch {
            dao.insert(
                ProfileEntity(
                    username = username,
                    uid = uid,
                    email = email
                )
            )
        }
    }

    fun updateData(data: ProfileEntity) {
        viewModelScope.launch {
            dao.update(data)
            _profileStateFlow.value = data // Pastikan UI diperbarui
        }
    }

    suspend fun getDataById(id: Int): ProfileEntity? {
        return withContext(Dispatchers.IO) {
            dao.getById(id)
        }
    }

    fun deleteData(data: ProfileEntity) {
        viewModelScope.launch {
            dao.delete(data)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val existingProfile = dao.getProfile()
            if (existingProfile == null) {
                val defaultProfile = ProfileEntity()
                dao.insert(defaultProfile)
                _profileStateFlow.value = defaultProfile
            } else {
                _profileStateFlow.value = existingProfile
            }
        }
    }

    fun updatePhotoPath(photoPath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updatePhotoPath(photoPath ?: "")

            // Load ulang data dari database agar profileStateFlow terupdate
            val updatedProfile = dao.getProfile()
            withContext(Dispatchers.Main) {
                _profileStateFlow.value = updatedProfile
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    val fileName = "profile_image.jpg"
    val file = File(context.filesDir, fileName)

    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        file.absolutePath // Kembalikan path untuk disimpan di database
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
