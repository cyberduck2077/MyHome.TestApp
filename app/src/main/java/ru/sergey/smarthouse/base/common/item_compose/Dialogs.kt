package ru.sergey.smarthouse.base.common.item_compose

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.sergey.smarthouse.base.theme.ThemeApp
import ru.sergey.smarthouse.base.theme.TextApp


@Composable
fun ExitOrNoDialog(
    title: String,
    anotherWay: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val activity = (LocalContext.current as? Activity)
    AlertDialog(
        shape = ThemeApp.shape.mediumAll,
        containerColor = ThemeApp.colors.background,
        titleContentColor = ThemeApp.colors.primary,
        textContentColor = ThemeApp.colors.primary,
        onDismissRequest = { onDismiss() },
        text = {
            Text(text = title)
        },
        title = {},
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = TextApp.buttonCancel)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                anotherWay?.let {
                    it()
                } ?: run {
                    activity?.finish()
                }
            }) {
                Text(text = TextApp.buttonGood)
            }
        })
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogApp(
    onDismiss: () -> Unit,
    containerColor: Color = Color.Black.copy(.4f),
    paddingContent: PaddingValues = PaddingValues(
        vertical = 80.dp,
        horizontal = 30.dp,
    ),
    content: @Composable (() -> Unit),
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            decorFitsSystemWindows = false,
        )) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss)
                .background(containerColor)
                .padding(paddingContent)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
