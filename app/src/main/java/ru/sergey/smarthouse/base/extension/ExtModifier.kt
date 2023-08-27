package ru.sergey.smarthouse.base.extension

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlin.math.ceil
import kotlin.math.roundToInt


@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

fun Modifier.minLinesHeight(
        minLines: Int,
        textStyle: TextStyle
                           ) = composed {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val resolvedStyle = remember(textStyle, layoutDirection) {
        resolveDefaults(textStyle, layoutDirection)
    }
    val resourceLoader = LocalFontFamilyResolver.current

    val heightOfTextLines = remember(
            density,
            textStyle,
            layoutDirection
                                    ) {
        val lines = (EmptyTextReplacement + "\n").repeat(minLines - 1)
        computeSizeForDefaultText(
                style = resolvedStyle,
                density = density,
                text = lines,
                maxLines = minLines,
                resourceLoader
                                 ).height
    }
    val heightInDp: Dp = with(density) { heightOfTextLines.toDp() }
    Modifier.defaultMinSize(minHeight = heightInDp)
}

fun computeSizeForDefaultText(
    style: TextStyle,
    density: Density,
    text: String = EmptyTextReplacement,
    maxLines: Int = 1,
    fontFamilyResolver: FontFamily.Resolver
                             ): IntSize {
    val paragraph = Paragraph(
            paragraphIntrinsics = ParagraphIntrinsics(
                    text = text,
                    style = style,
                    density = density,
                    fontFamilyResolver = fontFamilyResolver
                                                     ),
            maxLines = maxLines,
            constraints = Constraints(maxWidth = ceil(Float.POSITIVE_INFINITY).toInt()),
                             )

    return IntSize(paragraph.minIntrinsicWidth.ceilToIntPx(), paragraph.height.ceilToIntPx())
}

internal const val DefaultWidthCharCount = 5
internal val EmptyTextReplacement = "H".repeat(DefaultWidthCharCount)
internal fun Float.ceilToIntPx(): Int = ceil(this).roundToInt()
