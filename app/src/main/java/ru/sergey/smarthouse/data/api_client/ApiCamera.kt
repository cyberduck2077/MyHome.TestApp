package ru.sergey.smarthouse.data.api_client

import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.sergey.smarthouse.models.api.CameraResponse
import ru.sergey.smarthouse.models.api.DoorsResponse

class ApiCamera(private val client: Client) {

    suspend fun getCameras(): CameraResponse? {
        return try {
            val response = client.api.get(
                urlString = "/api/rubetek/cameras/"
            )
            response.body<CameraResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getDoors(): DoorsResponse? {
        return try {
            val response = client.api.get(
                urlString = "/api/rubetek/doors/"
            )
            response.body<DoorsResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}