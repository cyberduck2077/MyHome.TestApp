package ru.sergey.smarthouse.base.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val ShapesMain = ShapeSchemeMain(
        smallTop = RoundedCornerShape(topStart = DimApp.smallShapeSize, topEnd = DimApp.smallShapeSize),
        smallAll = RoundedCornerShape(DimApp.smallShapeSize),
        smallRight =  RoundedCornerShape(topEnd = DimApp.smallShapeSize, bottomEnd = DimApp.smallShapeSize),
        mediumTop = RoundedCornerShape(topStart = DimApp.mediumShapeSize, topEnd = DimApp.mediumShapeSize),
        mediumBottom = RoundedCornerShape(bottomStart = DimApp.mediumShapeSize, bottomEnd = DimApp.mediumShapeSize),
        mediumAll = RoundedCornerShape(DimApp.mediumShapeSize),
        mediumLeft = RoundedCornerShape(topStart = DimApp.mediumShapeSize, bottomStart = DimApp.mediumShapeSize),
        mediumRight = RoundedCornerShape(topEnd = DimApp.mediumShapeSize, bottomEnd = DimApp.mediumShapeSize),
                                )

val TypographyMain = TypographySchemeMain(
        titleMedium = TextStyle(
                fontWeight= FontWeight.Medium,
                fontSize = 20.sp),
        titleLarge = TextStyle(
                fontWeight= FontWeight.Bold,
                fontSize = 20.sp),
        titleSmall = TextStyle(
                fontWeight= FontWeight.Medium,
                fontSize = 16.sp),
        button = TextStyle(
                fontWeight= FontWeight.Bold,
                fontSize = 14.sp),
        bodySmall = TextStyle(
                fontWeight= FontWeight.Normal,
                fontSize = 14.sp),
        bodyLarge = TextStyle(
                fontWeight= FontWeight.Normal,
                fontSize = 16.sp),
        labelMedium = TextStyle(
                fontWeight= FontWeight.Bold,
                fontSize = 12.sp),
        caption = TextStyle(
                fontWeight= FontWeight.Normal,
                fontSize = 12.sp),
        labelSmall = TextStyle(
                fontWeight= FontWeight.Normal,
                fontSize = 10.sp))

@Immutable
data class ColorsSchemeMain(
        val primary: Color,
        val secondary: Color,
        val tertiary: Color,
        val quaternary: Color,
        val onPrimary: Color,
        val onPrimaryVariant: Color,
        val background: Color,
        val backgroundContainer: Color,
        val attentionContent: Color,
        val goodContent: Color,)


@Immutable
data class ShapeSchemeMain(
        val smallTop: CornerBasedShape,
        val smallAll: CornerBasedShape,
        val smallRight: CornerBasedShape,
        val mediumTop: CornerBasedShape,
        val mediumBottom: CornerBasedShape,
        val mediumLeft: CornerBasedShape,
        val mediumRight: CornerBasedShape,
        val mediumAll: CornerBasedShape,
                          )

@Immutable
data class TypographySchemeMain(
        val titleLarge: TextStyle,
        val titleMedium: TextStyle,
        val titleSmall: TextStyle,
        val button: TextStyle,
        val bodySmall: TextStyle,
        val bodyLarge: TextStyle,
        val labelMedium: TextStyle,
        val caption: TextStyle,
        val labelSmall: TextStyle,
                               )

val LocalColorsMain = staticCompositionLocalOf { LightPaletteMain }
val LocalTypographyMain = staticCompositionLocalOf { TypographyMain }
val LocalShapeMain = staticCompositionLocalOf { ShapesMain }


object ThemeApp {
    val colors: ColorsSchemeMain
        @Composable
        get() = LocalColorsMain.current
    val typography: TypographySchemeMain
        @Composable
        get() = LocalTypographyMain.current
    val shape: ShapeSchemeMain
        @Composable
        get() = LocalShapeMain.current
}
