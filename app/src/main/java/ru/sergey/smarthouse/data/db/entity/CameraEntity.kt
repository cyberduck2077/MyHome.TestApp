package ru.sergey.smarthouse.data.db.entity

import io.realm.kotlin.types.RealmObject

class CameraEntity : RealmObject {
    var id: Int = 0
    var favorites: Boolean? = null
    var name: String? = null
    var rec: Boolean? = null
    var room: String? = null
    var snapshot: String? = null
}