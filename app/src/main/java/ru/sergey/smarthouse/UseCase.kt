package ru.sergey.smarthouse

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import ru.sergey.smarthouse.base.common.logE
import ru.sergey.smarthouse.data.api_client.ApiCamera
import ru.sergey.smarthouse.data.db.entity.CameraEntity
import ru.sergey.smarthouse.data.db.entity.DoorEntity
import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.DataCamera
import ru.sergey.smarthouse.models.api.Door

class UseCase(
    private val api: ApiCamera,
    private val realm: Realm
) {
    suspend fun getCameras(
        flowStart: () -> Unit,
        flowError: () -> Unit,
        flowSuccess: (DataCamera) -> Unit,
    ) {
        flowStart.invoke()
        val response = try {
            api.getCameras()
        } catch (e: Exception) {
            val error = "getCameras ${e.message.toString()}"
            logE(error)
            e.printStackTrace()
            flowError.invoke()
            return
        }

        if (response?.success == false) {
            flowError.invoke()
            return
        }
        response?.data?.let { resp ->
            if (resp.cameras.isNotEmpty()) {
                realm.write {
                    resp.cameras.forEach {
                        val cameraR = CameraEntity()
                        cameraR.mappingResponse(it)
                        copyToRealm(cameraR)
                    }
                }
                flowSuccess.invoke(resp)
            }
            else {
                flowSuccess.invoke(mappingResponseFromDbCamera())
            }
        }
    }

    suspend fun getDoors(
        flowStart: () -> Unit,
        flowError: () -> Unit,
        flowSuccess: (List<Door>) -> Unit,
    ) {
        flowStart.invoke()
        val response = try {
            api.getDoors()
        } catch (e: Exception) {
            val error = "getDoors ${e.message.toString()}"
            logE(error)
            e.printStackTrace()
            flowError.invoke()
            return
        }

        if (response?.success == false) {
            flowError.invoke()
            return
        }
        response?.data?.let { resp ->
            if (resp.isNotEmpty()) {
                realm.write {
                    resp.forEach {
                        val doorR = DoorEntity()
                        doorR.mappingResponse(it)
                        copyToRealm(doorR)
                    }
                }
                flowSuccess.invoke(resp)
            }
            else {
                flowSuccess.invoke(mappingResponseFromDbDoor())
            }
        }
    }

    private fun mappingResponseFromDbDoor(): List<Door> {
        val all = realm.query<DoorEntity>().find().toList()
        val fromDb = mutableListOf<Door>()
        all.forEach {
            fromDb.add(
                Door(
                    favorites = it.favorites,
                    id = it.id,
                    name = it.name,
                    room = it.room,
                    snapshot = it.snapshot
                )
            )
        }
        return fromDb
    }

    private fun mappingResponseFromDbCamera(): DataCamera {
        val all = realm.query<CameraEntity>().find().toList()
        val fromDb = mutableListOf<Camera>()
        all.forEach {
            fromDb.add(
                Camera(
                    favorites = it.favorites,
                    id = it.id,
                    name = it.name,
                    rec = it.rec,
                    room = it.room,
                    snapshot = it.snapshot
                )
            )
        }
        return DataCamera(cameras = fromDb, room = listOf())
    }
}