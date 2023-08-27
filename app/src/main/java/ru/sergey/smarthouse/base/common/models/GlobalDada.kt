package ru.sergey.smarthouse.base.common.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.sergey.smarthouse.base.common.EventProject
import ru.sergey.smarthouse.base.common.SingleLiveEvent

object GlobalDada {
    var appDynamicSettings = SingleLiveEvent<ApplicationSettings>()
}

data class ApplicationSettings(
    val isLoad: Boolean = false,
    val heightNavPanel: Dp = 0.dp,
    val messageCenter: EventProject<String?> = EventProject(null),
)

fun gDNavHeight(dp: Dp) {
    GlobalDada.appDynamicSettings.value =
        GlobalDada.appDynamicSettings.value?.copy(heightNavPanel = dp)
            ?: ApplicationSettings().copy(heightNavPanel = dp)
}

fun gDMessage(text: String?) {
    GlobalDada.appDynamicSettings.value =
        GlobalDada.appDynamicSettings.value?.copy(messageCenter = EventProject(
            text))
            ?: ApplicationSettings().copy(messageCenter = EventProject(null))
}

fun globalDynamicSetLoader(isLoad: Boolean) {
    GlobalDada.appDynamicSettings.value =
        GlobalDada.appDynamicSettings.value?.copy(isLoad = isLoad)
            ?: ApplicationSettings().copy(isLoad = isLoad)
}