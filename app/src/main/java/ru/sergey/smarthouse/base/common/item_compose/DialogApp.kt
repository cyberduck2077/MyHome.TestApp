package ru.sergey.smarthouse.base.common.item_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import ru.sergey.smarthouse.base.theme.DimApp
import ru.sergey.smarthouse.base.theme.TextApp
import ru.sergey.smarthouse.base.theme.ThemeApp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogApp(
    oldValue: String,
    newValue: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        var value by remember { mutableStateOf(TextFieldValue(text = oldValue)) }
        Column(
            modifier = Modifier
                .clip(ThemeApp.shape.mediumAll)
                .background(ThemeApp.colors.background)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = DimApp.screenPadding),
                textAlign = TextAlign.Center,
                text = TextApp.textNewValue,
                style = ThemeApp.typography.titleSmall,
                color = ThemeApp.colors.primary
            )
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), onClick = onDismiss
                ) {
                    Text(text = TextApp.buttonCancel)
                }

                Box(modifier = Modifier.size(DimApp.screenPadding))

                TextButton(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), onClick = {
                    newValue.invoke(value.text)
                }) {
                    Text(text = TextApp.buttonGood)
                }
            }
        }
    }
}