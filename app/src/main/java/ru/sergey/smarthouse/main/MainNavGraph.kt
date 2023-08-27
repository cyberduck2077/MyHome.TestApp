package ru.sergey.smarthouse.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.launch
import ru.sergey.smarthouse.base.common.item_compose.LoadBarWithTimerClose
import ru.sergey.smarthouse.base.common.logE
import ru.sergey.smarthouse.base.common.models.ApplicationSettings
import ru.sergey.smarthouse.base.theme.ThemeApp
import ru.sergey.smarthouse.base.theme.DimApp
import ru.sergey.smarthouse.screen.MainScreen

const val AppNavGraph = "AppNavGraph"
@Composable
fun MainNavGraph(
    navController: NavHostController,
    applicationSettings: MutableState<ApplicationSettings>
) {

    val snackBarHostStateBottomCenterOnBar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(applicationSettings.value.messageCenter) {
        scope.launch {
            val text = applicationSettings.value.messageCenter.value
            if (!text.isNullOrEmpty())
                snackBarHostStateBottomCenterOnBar.showSnackbar(
                    message = text,
                    duration = SnackbarDuration.Short
                )
        }
    }



    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val bottomPadding = if (imeBottomPadding > systemBottomPadding)
        (imeBottomPadding - systemBottomPadding) else 0.dp

    logE(imeBottomPadding)
    logE(systemBottomPadding)
    Box(
        modifier = Modifier
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
            .fillMaxSize()
            .padding(bottom = bottomPadding)
    ) {

        NavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = MainScreen.route,
            route = AppNavGraph
        ) {
            MainScreen(this, navController).addRoute()
        }

        LoadBarWithTimerClose(isView = applicationSettings.value.isLoad)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SnackbarHost(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = DimApp.navBottomBarHeight),
                hostState = snackBarHostStateBottomCenterOnBar
            )
        }
    }
}
