package ru.sergey.smarthouse

import ru.sergey.smarthouse.base.common.logE
import ru.sergey.smarthouse.data.api_client.ApiCamera
import ru.sergey.smarthouse.data.db.DbWorker
import ru.sergey.smarthouse.models.api.DataCamera
import ru.sergey.smarthouse.models.api.Door

class UseCase(private val api: ApiCamera) {
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
        response?.data?.let {
            flowSuccess.invoke(it)
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
        response?.data?.let { flowSuccess.invoke(it) }
    }
}