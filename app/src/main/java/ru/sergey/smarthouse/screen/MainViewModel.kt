package ru.sergey.smarthouse.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.sergey.smarthouse.UseCase
import ru.sergey.smarthouse.base.common.BaseViewModel
import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.DataCamera
import ru.sergey.smarthouse.models.api.Door


class MainViewModel(private val useCase: UseCase) : BaseViewModel() {

    private val _liveDataDoor = MutableLiveData<List<Door>>()
    val liveDataDoor: LiveData<List<Door>> = _liveDataDoor

    private val _liveDataCamera = MutableLiveData<List<Camera>>()
    val liveDataCamera: LiveData<List<Camera>> = _liveDataCamera

    fun getDoors() = viewModelScope.launch {
        useCase.getDoors(
            flowStart = {},
            flowError = {},
            flowSuccess = { _liveDataDoor.value = it })
    }

    fun getCameras() = viewModelScope.launch {
        useCase.getCameras(
            flowStart = {},
            flowError = {},
            flowSuccess = { _liveDataCamera.value = it.cameras })
    }

}