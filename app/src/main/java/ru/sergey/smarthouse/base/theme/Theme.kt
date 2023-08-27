package ru.sergey.smarthouse.base.theme

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@Composable
fun MainTheme(
    colors: ColorsSchemeMain = LightPaletteMain,
    typography: TypographySchemeMain = TypographyMain,
    shape: ShapeSchemeMain = ShapesMain,
    content: @Composable () -> Unit
             ) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    CompositionLocalProvider(
        LocalColorsMain provides colors,
        LocalTypographyMain provides typography,
        LocalShapeMain provides shape,
        content = content
    )
}