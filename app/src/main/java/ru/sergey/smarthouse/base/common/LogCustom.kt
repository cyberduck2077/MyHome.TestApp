package ru.sergey.smarthouse.base.common

import android.util.Log
import ru.sergey.smarthouse.BuildConfig


private const val mainStr = "PROJECT_SMART_HOUSE $APP_TAG"
private val SHOW_LOG = BuildConfig.DEBUG

fun logI(vararg any: Any) {
    if (!SHOW_LOG) return
    Log.i(mainStr, any.toList().toString())
}

fun logE(vararg any: Any) {
    if (!SHOW_LOG) return
    Log.e(mainStr, any.toList().toString())
}

fun logD(vararg any: Any) {
    if (!SHOW_LOG) return
    Log.d(mainStr, any.toList().toString())
}

fun logW(vararg any: Any) {
    if (!SHOW_LOG) return
    Log.w(mainStr, any.toList().toString())
}

fun logV(vararg any: Any) {
    if (!SHOW_LOG) return
    Log.v(mainStr, any.toList().toString())
}