package com.example.launcher_for_tv.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.launcher_for_tv.data.AppModel
import com.example.launcher_for_tv.data.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _isHideBackground = MutableStateFlow(false)
    val isHideBackground = _isHideBackground.asStateFlow()

    fun setHideBackground(isHide: Boolean) {
        viewModelScope.launch {
            _isHideBackground.value = isHide
        }
    }

    private val repository = AppRepository(application)
    private val _installedApps = MutableStateFlow<List<AppModel>>(emptyList())
    val installedApps: StateFlow<List<AppModel>> = _installedApps.asStateFlow()

    private val appContext = application

    init {
        viewModelScope.launch {
            repository.getInstalledApps().collect { apps ->
                val filteredApps = apps.filter { it.packageName != appContext.packageName }
                _installedApps.value = filteredApps
            }
        }
    }

    fun launchApp(packageName: String) {
        val intent = appContext.packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}
