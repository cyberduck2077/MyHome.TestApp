package ru.sergey.smarthouse.base.common.item_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import ru.sergey.smarthouse.base.theme.ThemeApp
import ru.sergey.smarthouse.base.theme.DimApp

@Composable
fun LoadBarWithTimerClose(
    isView: Boolean,
    modifier: Modifier = Modifier,
) {
    val stateIsView = remember(isView){ mutableStateOf(isView) }
    if (stateIsView.value) {
        LaunchedEffect(Unit){
            delay(20_00)
            stateIsView.value = false
        }

        Dialog(onDismissRequest = {}) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(20))
                        .background(ThemeApp.colors.background)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(DimApp.screenPadding),
                        color = ThemeApp.colors.primary
                    )
                }
            }
        }
    }
}