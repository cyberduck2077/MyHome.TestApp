package ru.sergey.smarthouse.base.common.item_compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sergey.smarthouse.base.extension.minLinesHeight
import ru.sergey.smarthouse.base.theme.ThemeApp


@Composable
fun TextFieldApp(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    placeholder: @Composable (() -> Unit)? = null,
    isPlaceholderCollapse: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    onClickContainer: (() -> Unit)? = null,
    isError: Boolean = false,
    isEnabledBorder: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    minLinesForeHeight: Int = 3,
    alignmentPlaceholder: Alignment = Alignment.CenterStart,
    alignmentTrailingIcon: Alignment = Alignment.CenterEnd,
    alignmentLeadingIcon: Alignment = Alignment.CenterStart,
    alignmentText: Alignment.Vertical = Alignment.CenterVertically,
    paddingVerticalForeText: Dp = 8.dp,
    paddingHorizontalForeText: Dp = 12.dp,
    textColor: Color = ThemeApp.colors.onPrimaryVariant,
    borderColor: Color = ThemeApp.colors.primary,
    noActiveColor: Color = ThemeApp.colors.onPrimaryVariant.copy(.4f),
    disabledColor: Color = ThemeApp.colors.onPrimaryVariant,
    placeholderContainerColor: Color = ThemeApp.colors.background,
    attentionColor: Color = ThemeApp.colors.attentionContent,
    shape: CornerBasedShape = ThemeApp.shape.smallAll,
    containerColorCommon: Color = ThemeApp.colors.background.copy(0F),
    containerColor: Color = ThemeApp.colors.background,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    paddingForePlaceholderTextInput: Dp = 20.dp,
    collapsingPlaceholderTextUnit: TextUnit = 11.sp,
    paddingForeTopTextPlaceHolder: Dp = 8.dp,
    sizeStrokeWidthLoaderHomeScreen: Dp = 2.dp,
    sizeUnableIndicatorLine: Dp = 1.dp,
                        ) {
    val focused by interactionSource.collectIsFocusedAsState()
    val indicatorColorFinal = animateColorAsState(
            targetValue = if (isError) {
                attentionColor
            } else if (enabled && focused) {
                borderColor
            } else if (enabled) {
                noActiveColor
            } else {
                disabledColor
            }, animationSpec = tween(200)
                                                 )

    val indicatorLineSize =
            if (focused || !enabled) {
                sizeStrokeWidthLoaderHomeScreen
            } else {
                sizeUnableIndicatorLine
            }

    BasicTextField(
            value = value,
            modifier = modifier,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.merge(TextStyle(color = if (enabled) textColor else noActiveColor)),
            cursorBrush = SolidColor(if (isError) attentionColor else textColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            decorationBox = @Composable { innerTextField ->

                val localDensity = LocalDensity.current
                val leadingIconHeightDp = remember { mutableStateOf(0.dp) }
                val leadingIconWidthDp = remember { mutableStateOf(0.dp) }

                val trailingIconHeightDp = remember { mutableStateOf(0.dp) }
                val trailingIconWidthDp = remember { mutableStateOf(0.dp) }
                val clickModifier = if (onClickContainer != null) Modifier
                    .clickable(
                            onClick = onClickContainer,
                            role = Role.Button,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                    bounded = true,
                                                       )) else Modifier


                val borderModifier = if (isEnabledBorder) Modifier.border(
                        width = indicatorLineSize,
                        color = indicatorColorFinal.value,
                        shape = shape) else Modifier
                Box(
                        modifier = Modifier
                            .background(containerColorCommon)) {

                    Column(
                            modifier = Modifier.fillMaxWidth()) {

                        Box(
                                modifier = Modifier.fillMaxWidth()) {

                            Row(
                                    modifier = Modifier
                                        .padding(top = paddingForeTopTextPlaceHolder)
                                        .fillMaxWidth()
                                        .background(containerColor, shape)
                                        .then(borderModifier)
                                        .clip(shape)
                                        .minLinesHeight(minLines = minLinesForeHeight,
                                                        textStyle = textStyle)
                                        .then(clickModifier)
                                        .padding(
                                                start = leadingIconWidthDp.value,
                                                end = trailingIconWidthDp.value
                                                )
                                        .padding(vertical = paddingVerticalForeText,
                                                 horizontal = paddingHorizontalForeText),
                                    verticalAlignment = alignmentText,
                               ) {
                                innerTextField()
                            }


                            if (leadingIcon != null) {
                                Box(
                                        modifier = Modifier
                                            .padding(
                                                    top = paddingForeTopTextPlaceHolder,
                                                    start = paddingForeTopTextPlaceHolder
                                                    )
                                            .align(alignmentLeadingIcon)
                                            .onGloballyPositioned { coordinates ->
                                                leadingIconHeightDp.value =
                                                        with(localDensity) { coordinates.size.height.toDp() }
                                                leadingIconWidthDp.value =
                                                        with(localDensity) { coordinates.size.width.toDp() }
                                            },
                                        contentAlignment = Alignment.Center
                                   ) {
                                    leadingIcon()
                                }
                            }
                            if (trailingIcon != null && enabled) {
                                Box(
                                        modifier = Modifier
                                            .padding(
                                                    top = paddingForeTopTextPlaceHolder,
                                                    end = paddingForeTopTextPlaceHolder
                                                    )
                                            .align(alignmentTrailingIcon)
                                            .onGloballyPositioned { coordinates ->
                                                trailingIconHeightDp.value =
                                                        with(localDensity) { coordinates.size.height.toDp() }
                                                trailingIconWidthDp.value =
                                                        with(localDensity) { coordinates.size.width.toDp() }
                                            },
                                        contentAlignment = Alignment.Center
                                   ) {
                                    trailingIcon()
                                }
                            }

                            if (placeholder != null) {
                                if (value.text.isBlank()) {
                                    val colorText = if (enabled) {
                                        noActiveColor
                                    } else {
                                        disabledColor
                                    }
                                    Box(
                                            modifier = Modifier
                                                .align(alignmentPlaceholder)
                                                .padding(
                                                        top = paddingForeTopTextPlaceHolder,
                                                        start = leadingIconWidthDp.value,
                                                        end = trailingIconWidthDp.value
                                                        )
                                                .padding(vertical = paddingVerticalForeText,
                                                         horizontal = paddingHorizontalForeText),
                                            contentAlignment = Alignment.Center
                                       ) {
                                        CompositionLocalProvider(
                                                LocalTextStyle provides textStyle.copy(
                                                        color = colorText
                                                                                      )
                                                                ) {
                                            placeholder()
                                        }

                                    }
                                } else if (isPlaceholderCollapse) {
                                    val colorText = if (enabled) {
                                        indicatorColorFinal.value
                                    } else {
                                        noActiveColor
                                    }
                                    Box(
                                            modifier = Modifier
                                                .padding(horizontal = paddingForePlaceholderTextInput)
                                                .background(placeholderContainerColor)
                                                .align(Alignment.TopStart),
                                            contentAlignment = Alignment.TopStart
                                       ) {
                                        CompositionLocalProvider(
                                                LocalTextStyle provides textStyle.copy(
                                                        fontSize = collapsingPlaceholderTextUnit,
                                                        color = colorText
                                                                                      )
                                                                ) {
                                            placeholder()
                                        }
                                    }
                                }
                            }
                        }

                        if (supportingText != null) {
                            Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = paddingHorizontalForeText),
                                    verticalAlignment = alignmentText,
                               ) {
                                CompositionLocalProvider(
                                        LocalTextStyle provides textStyle.copy(
                                                fontSize = 12.sp,
                                                color = indicatorColorFinal.value
                                                                              ),
                                                        ) {
                                    supportingText()
                                }
                            }
                        }
                    }
                }
            })
}

@Composable
fun TextFieldAppSimpleString(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ThemeApp.typography.bodyLarge,
    placeholder: @Composable (() -> Unit)? = null,
    isPlaceholderCollapse: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    onClickContainer: (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    minLinesForeHeight: Int = 3,
    alignmentPlaceholder: Alignment = Alignment.CenterStart,
    alignmentTrailingIcon: Alignment = Alignment.CenterEnd,
    alignmentLeadingIcon: Alignment = Alignment.CenterStart,
    alignmentText: Alignment.Vertical = Alignment.CenterVertically,
    paddingVerticalForeText: Dp = 8.dp,
    paddingHorizontalForeText: Dp = 12.dp,
    isEnabledBorder: Boolean = true,
    textColor: Color = ThemeApp.colors.onPrimaryVariant,
    borderColor: Color = ThemeApp.colors.onPrimaryVariant,
    noActiveColor: Color = ThemeApp.colors.onPrimaryVariant,
    disabledColor: Color = ThemeApp.colors.onPrimaryVariant,
    placeholderContainerColor: Color = ThemeApp.colors.background,
    attentionColor: Color = ThemeApp.colors.attentionContent,
    shape: CornerBasedShape = ThemeApp.shape.smallAll,
    containerColorCommon: Color = ThemeApp.colors.background.copy(0F),
    containerColor: Color = ThemeApp.colors.backgroundContainer,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    paddingForePlaceholderTextInput: Dp = 20.dp,
    paddingForeTopTextPlaceHolder: Dp = 8.dp,
    sizeStrokeWidthLoaderHomeScreen: Dp = 2.dp,
    sizeUnableIndicatorLine: Dp = 1.dp,
                        ) {

    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    var lastTextValue by remember(value) { mutableStateOf(value) }
    TextFieldApp(
        value = textFieldValue,
        onValueChange = { newTextFieldValueState ->
                textFieldValueState = newTextFieldValueState

                val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValueState.text
                lastTextValue = newTextFieldValueState.text

                if (stringChangedSinceLastInvocation) {
                    onValueChange(newTextFieldValueState.text)
                }
            },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        isPlaceholderCollapse = isPlaceholderCollapse,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        onClickContainer = onClickContainer,
        isEnabledBorder = isEnabledBorder,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLinesForeHeight = minLinesForeHeight,
        alignmentPlaceholder = alignmentPlaceholder,
        alignmentTrailingIcon = alignmentTrailingIcon,
        alignmentLeadingIcon = alignmentLeadingIcon,
        alignmentText = alignmentText,
        paddingVerticalForeText = paddingVerticalForeText,
        paddingHorizontalForeText = paddingHorizontalForeText,
        textColor = textColor,
        borderColor = borderColor,
        noActiveColor = noActiveColor,
        disabledColor = disabledColor,
        placeholderContainerColor = placeholderContainerColor,
        attentionColor = attentionColor,
        shape = shape,
        containerColorCommon = containerColorCommon,
        containerColor = containerColor,
        interactionSource = interactionSource,
        paddingForePlaceholderTextInput = paddingForePlaceholderTextInput,
        paddingForeTopTextPlaceHolder = paddingForeTopTextPlaceHolder,
        sizeStrokeWidthLoaderHomeScreen = sizeStrokeWidthLoaderHomeScreen,
        sizeUnableIndicatorLine = sizeUnableIndicatorLine,
                        )
}
