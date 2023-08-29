package ru.sergey.smarthouse.data.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import ru.sergey.smarthouse.data.db.entity.CameraEntity
import ru.sergey.smarthouse.data.db.entity.DoorEntity

class DbWorker() {
    private val realm: Realm by lazy {
        Realm.open(
            RealmConfiguration.Builder(
                schema = setOf(
                    CameraEntity::class,
                    DoorEntity::class
                )
            )
                .name("default.realm")
                .build()
        )
    }

        fun getInstance() = realm

}
