package ru.sergey.smarthouse.models.api

import ru.sergey.smarthouse.data.db.entity.DoorEntity

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