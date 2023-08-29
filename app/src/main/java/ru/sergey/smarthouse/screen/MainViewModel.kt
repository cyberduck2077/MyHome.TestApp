package ru.sergey.smarthouse.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch
import ru.sergey.smarthouse.UseCase
import ru.sergey.smarthouse.base.common.BaseViewModel
import ru.sergey.smarthouse.base.common.logD
import ru.sergey.smarthouse.data.db.entity.CameraEntity
import ru.sergey.smarthouse.data.db.entity.DoorEntity
import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.DataCamera
import ru.sergey.smarthouse.models.api.Door


class MainViewModel(private val useCase: UseCase, private val realm: Realm) : BaseViewModel() {

    private val _liveDataDoor = MutableLiveData<List<Door>>()
    val liveDataDoor: LiveData<List<Door>> = _liveDataDoor

    private val _liveDataCamera = MutableLiveData<List<Camera>>()
    val liveDataCamera: LiveData<List<Camera>> = _liveDataCamera

    fun getDoors() = viewModelScope.launch {
        useCase.getDoors(
            flowStart = {},
            flowError = {},
            flowSuccess = {
                _liveDataDoor.value = it
            })
    }

    fun getCameras() = viewModelScope.launch {
        useCase.getCameras(
            flowStart = {},
            flowError = {},
            flowSuccess = {
                _liveDataCamera.value = it.cameras
            })
    }

}