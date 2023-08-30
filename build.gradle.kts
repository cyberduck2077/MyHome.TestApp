// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlin = "1.8.21"
    val androidApplication = "8.1.0"
    val realm = "1.10.0"

    id ("com.android.application") version androidApplication apply false
    id ("org.jetbrains.kotlin.android") version kotlin apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version kotlin apply false
    id ("io.realm.kotlin") version realm apply false
}