package ru.sergey.smarthouse.base.common

data class EventProject<out T>(
    val value: T,
    private val id: Int = if (lastId == Int.MAX_VALUE) {
        lastId = Int.MIN_VALUE
        Int.MAX_VALUE
    } else lastId++,
) {
    companion object {
        private var lastId = Int.MAX_VALUE
    }

    private var valueSent = false

    fun getValueOnce(): T? {
        return if (!valueSent) {
            valueSent = true
            value
        } else null
    }
}