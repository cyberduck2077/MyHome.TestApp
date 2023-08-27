package ru.sergey.smarthouse.utils

import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.Door

val MOCK_DOORS = listOf<Door>(
    Door(
        favorites = false,
        id = 1,
        name = "Door name 1",
        room = "room 1",
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    ),
    Door(
        favorites = true,
        id = 2,
        name = "Door name 3",
        room = "room 2",
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    ),
    Door(
        favorites = false,
        id = 3,
        name = "Door name 3",
        room = "room 3",
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    )
)

val MOCK_CAMERAS = listOf<Camera>(
    Camera(
        favorites = true,
        id = 0,
        name = "Camera name 1",
        rec = true,
        room = "2",
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    ),
    Camera(
        favorites = false,
        id = 1,
        name = "Camera name 1",
        rec = true,
        room = "1",
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    ),
    Camera(
        favorites = true,
        id = 2,
        name = "Camera name 1",
        rec = false,
        room = "3",
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    ),
)