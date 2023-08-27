package ru.sergey.smarthouse.base.common.item_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.sergey.smarthouse.base.theme.ThemeApp


@Composable
fun TextButtonApp(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit,
    paddingText: PaddingValues = PaddingValues(16.dp),
    isEnabled: Boolean = true,
    style: TextStyle = ThemeApp.typography.button,
    textAlign: TextAlign = TextAlign.Center,
    contentColor: Color = ThemeApp.colors.onPrimary,
    backgroundColor: Color = ThemeApp.colors.onPrimary.copy(0f)
) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .fillMaxWidth()
            .clickable(
                enabled = isEnabled,
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingText),
            textAlign = textAlign,
            softWrap = false,
            style = style,
            color = contentColor,
            text = text)
    }
}
