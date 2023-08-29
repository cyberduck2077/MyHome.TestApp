package ru.sergey.smarthouse.data.db.entity

import io.realm.kotlin.types.RealmObject
import ru.sergey.smarthouse.models.api.Camera

class CameraEntity : RealmObject {
    var id: Int = 0
    var favorites: Boolean? = null
    var name: String? = null
    var rec: Boolean? = null
    var room: String? = null
    var snapshot: String? = null

    fun mappingResponse(camera: Camera) {
       this.id = camera.id
       this.favorites = camera.favorites
       this.name = camera.name
       this.rec = camera.rec
       this.room = camera.room
       this.snapshot = camera.snapshot
    }
}