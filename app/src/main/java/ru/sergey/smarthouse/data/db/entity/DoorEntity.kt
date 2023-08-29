package ru.sergey.smarthouse.data.db.entity

import io.realm.kotlin.types.RealmObject
import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.Door

class DoorEntity : RealmObject {
    var id: Int = 0
    var favorites: Boolean? = null
    var name: String? = null
    var room: String? = null
    var snapshot: String? = null

    fun mappingResponse(door: Door) {
        this.id = door.id
        this.favorites = door.favorites
        this.name = door.name
        this.room = door.room
        this.snapshot = door.snapshot
    }
}