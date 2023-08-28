package ru.sergey.smarthouse.models.api

data class CameraResponse(
    val `data`: DataCamera,
    val success: Boolean
)

data class DataCamera(
    val cameras: List<Camera>,
    val room: List<String>
)

data class Camera(
    val favorites: Boolean?,
    val id: Int,
    val name: String?,
    val rec: Boolean?,
    val room: String?,
    val snapshot: String?
)