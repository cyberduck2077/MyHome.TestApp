package ru.sergey.smarthouse.models.api

data class DoorsResponse(
    val `data`: List<Door>,
    val success: Boolean
)

data class Door(
    val favorites: Boolean?,
    val id: Int,
    val name: String?,
    val room: String?,
    val snapshot: String?
)