//package ru.sergey.smarthouse.base.common.item_compose
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.ripple.rememberRipple
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Dp
//import ru.sergey.smarthouse.R
//import ru.sergey.smarthouse.base.theme.ThemeApp
//import ru.sergey.smarthouse.base.theme.dimApp
//
//@Composable
//fun TopPanelBack(
//    text: String,
//    onClick: () -> Unit,
//    contentColor: Color = ThemeApp.colors.onPrimaryVariant,
//    containerColor: Color = ThemeApp.colors.background,
//    heightPanel: Dp = dimApp.topPanelHeight,
//    textStyle: TextStyle = ThemeApp.typography.titleSmall,
//    textAlign: TextAlign = TextAlign.Start
//) {
//
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .height(heightPanel)
//        .background(containerColor)) {
//
//        IconButtonApp(
//            modifier = Modifier.align(Alignment.CenterStart),
//            onClick = onClick,
//            icon = painterResource(id = R.drawable.ic_arrow_back),
//            contentColor = contentColor)
//
//        Text(
//            modifier = Modifier
//                .align(Alignment.Center)
//                .fillMaxWidth()
//                .padding(horizontal = dimApp.iconSizeTouchStandard),
//            textAlign = textAlign,
//            color =contentColor,
//            style =textStyle,
//            text =text)
//    }
//}
