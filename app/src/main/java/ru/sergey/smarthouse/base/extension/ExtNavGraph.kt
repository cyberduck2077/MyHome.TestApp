package ru.sergey.smarthouse.base.extension

fun String.setNameNavArguments(vararg str: String): String {
    if (str.isEmpty()) return  this
    var stringConcat = this
    str.forEachIndexed { index, s ->
        stringConcat += if (index == 0) "?$s={$s}" else ",$s={$s}"
    }
    return stringConcat
}

fun sendArgs(name: String,arg: Any?): String {
    return buildString {
        append("${name}=")
        append("$arg")
    }
}