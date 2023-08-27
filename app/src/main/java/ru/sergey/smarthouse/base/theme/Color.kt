package ru.sergey.smarthouse.base.theme

import androidx.compose.ui.graphics.Color

val Sapphire = Color(0xFF0F1E34)
val SlateGray = Color(0xFF6B717B)
val MatteWhite = Color(0xFFF9FAFA)
val GreenJungleCrayola = Color(0xFF20B085)
val BlueAzure = Color(0xFF2A48DF)
val DarkBluishGreen = Color(0xFF01432F)
val SelagoWhite = Color(0xFFFAF9FA)
val WhiteLilacBG = Color(0xFFEDE9F1)

val Crimson = Color(0xFFC22F2F)
val Emerald = Color(0xFF2FC27E)

val LightPaletteMain = ColorsSchemeMain(
        primary = BlueAzure,
        secondary = DarkBluishGreen,
        tertiary = SlateGray,
        onPrimary = Color.Black,
        onPrimaryVariant = Sapphire,
        background = WhiteLilacBG,
        backgroundContainer = SelagoWhite,
        quaternary = MatteWhite,
        attentionContent = Crimson,
        goodContent = Emerald,)