package ru.sergey.smarthouse.base.common

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import ru.sergey.smarthouse.base.common.item_compose.ExitOrNoDialog


@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    DisposableEffect(key1 = backPressedDispatcher, effect = {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    })
}

@Composable
fun BackPressExit(
    title: String,
    anotherWay: (() -> Unit)? = null,
) {
    val exitCheck = remember { mutableStateOf(false) }
    BackPressHandler { exitCheck.value = true }
    if (exitCheck.value) ExitOrNoDialog(
        title = title, anotherWay = anotherWay) {
        exitCheck.value = false
    }
}

