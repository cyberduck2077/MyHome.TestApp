package ru.sergey.smarthouse.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sergey.smarthouse.base.common.models.ApplicationSettings
import ru.sergey.smarthouse.base.common.models.GlobalDada
import ru.sergey.smarthouse.base.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        GlobalDada.appDynamicSettings.value = ApplicationSettings()
        setContent {
            val navController: NavHostController = rememberNavController()
            val applicationSettings = remember { mutableStateOf(ApplicationSettings()) }
            GlobalDada.appDynamicSettings.observe(this) {
                applicationSettings.value = it
            }
            MainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavGraph(
                        navController = navController,
                        applicationSettings = applicationSettings
                    )
                }
            }
        }
    }
}
