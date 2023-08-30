package ru.sergey.smarthouse.base.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val DimApp = Dimensions()

data class Dimensions(
        val lineHeight: Dp = 1.dp,
        val lineHeightIsOnline: Dp = 3.dp,
        val lineHeightBig: Dp = 4.dp,

        val baseElevation: Dp = 9.dp,

        val screenPadding: Dp = 16.dp,
        val screenPaddingHorrizontal: Dp = 8.dp,
        val crazyPadding: Dp = 12.dp,
        val textProfileNamePaddingTop: Dp = 66.dp,

        val avatarProfileTopPadding: Dp = 50.dp,
        val avatarProfileSize: Dp = 100.dp,
        val avatarInOrdersSize: Dp = 60.dp,
        val avatarStores: Dp = 44.dp,
        val avatarServices: Dp = 30.dp,
        val avatarOnlineProfileSize: Dp = 18.dp,

        val paddingTopForeQR: Dp = 60.dp,

        val textPaddingMin: Dp = 4.dp,
        val indicatorPadding : Dp = 30.dp,

        val pagerPaddingForeCard: Dp = 20.dp,

        val starsPadding : Dp = 4.dp,
        val badgeLittle: Dp = 5.dp,

        val menuItemsWidth:Dp = 22.dp,
        val menuItemsHeight:Dp = 3.dp,

        val iconSizeSmale: Dp = 13.dp,
        val iconSizeStandard: Dp = 25.dp,
        val iconSizeTouchStandard: Dp = 48.dp,
        val iconSizeBig: Dp = 38.dp,
        val iconSizeLittle: Dp = 18.dp,


        val imageAddSize: Dp = 100.dp,
        val paddingInIconAddPhoto: Dp = 8.dp,
    //Button
        val indicatorButtonLineSize: Dp = 2.dp,
        val buttonPortugalHeight: Dp = 40.dp,
        val fabSize: Dp = 56.dp,
        val fabPaddingForeShadow: Dp = 4.dp,

        val navBottomBarHeight: Dp = 80.dp,
        val topPanelHeight: Dp = 56.dp,
        val topPanelButtonsNav: Dp = 46.dp,
        val splashLogoHeight: Dp = 60.dp,

        val  mediumShapeSize: Dp = 20.dp,
        val  smallShapeSize: Dp = 6.dp,


        )