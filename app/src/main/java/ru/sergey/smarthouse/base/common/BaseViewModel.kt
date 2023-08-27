package ru.sergey.smarthouse.base.common

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.sergey.smarthouse.base.common.models.GlobalDada
import ru.sergey.smarthouse.base.common.models.gDMessage
import ru.sergey.smarthouse.base.common.models.globalDynamicSetLoader


abstract class BaseViewModel : ViewModel() {

    private val navController = MutableStateFlow<NavHostController?>(null)

    private val _message = MutableStateFlow<EventProject<String>?>(null)
    val message: StateFlow<EventProject<String>?> get() = _message

    private val _exitApp = MutableStateFlow<Boolean>(false)
    val exitApp: StateFlow<Boolean> get() = _exitApp

    fun getNameModel() = this::class.java.name ?: "BaseViewModel"

    open fun startLoaderDialog() {
        globalDynamicSetLoader(isLoad = true)
        logE("LoaderDialog startLoaderDialog() ${GlobalDada.appDynamicSettings.value}")
    }

    open fun closeLoaderDialog() {
        globalDynamicSetLoader(isLoad = false)
        logE("LoaderDialog startLoaderDialog() ${GlobalDada.appDynamicSettings.value}")
    }


    protected open fun setLoading(isLoad: Boolean) {
        if (isLoad) startLoaderDialog() else closeLoaderDialog()
    }

    protected fun exitApp() {
        _exitApp.value = true
    }

    protected fun noExitApp() {
        _exitApp.value = false
    }

    fun goBackStack() {
        navController.value?.popBackStack()
    }

    fun initStartScreen(controller: NavHostController) {
        navController.value = controller
    }

    protected fun toastLong(activity: Activity, text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    protected fun messageBottomCenterOnBar(message: String) = viewModelScope.launch {
        gDMessage(message)
        delay(1000L)
        gDMessage(null)
    }

}