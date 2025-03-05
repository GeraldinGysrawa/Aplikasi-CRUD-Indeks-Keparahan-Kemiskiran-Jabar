package com.example.hanyarunrun.viewmodel

import android.app.Application
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

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).profileDao()
    val dataList: LiveData<List<ProfileEntity>> = dao.getAll()

    var profile: ProfileEntity? = null

    private val _profileStateFlow = MutableStateFlow<ProfileEntity?>(null)
    val profileStateFlow: StateFlow<ProfileEntity?> = _profileStateFlow

    init {
        viewModelScope.launch {
            _profileStateFlow.value = dao.getProfile() ?: ProfileEntity()
        }
    }

    init {
        loadProfile()
    }


    fun insertData(
        username: String,
        uid: String,
        email: String
    ) {
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
            _profileStateFlow.value = data // Update state to trigger recomposition
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
                // If no profile exists, insert default profile
                val defaultProfile = ProfileEntity()
                dao.insert(defaultProfile)
                _profileStateFlow.value = defaultProfile
            } else {
                _profileStateFlow.value = existingProfile
            }
        }
    }
}