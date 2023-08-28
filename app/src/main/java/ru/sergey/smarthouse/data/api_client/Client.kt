package ru.sergey.smarthouse.data.api_client

import android.util.Log
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser.parseString
import com.google.gson.JsonSyntaxException
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.takeFrom
import io.ktor.serialization.jackson.jackson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.json.Json
import ru.sergey.smarthouse.BuildConfig
import ru.sergey.smarthouse.base.common.logI

class Client() : Closeable {
    companion object {
        const val BASE_URL: String = "http://cars.cprogroup.ru"
    }

    val api = HttpClient {
        install(ContentNegotiation) {
            jackson {
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
                setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = false // false
                    isLenient = true
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                    prettyPrint = false
                    useArrayPolymorphism = false
                }
            )
        }

        install(Logging) {
            logger = loggerPretty
            level = LogLevel.ALL
        }
        HttpResponseValidator {
            validateResponse {
                logI("APIClient", "HttpResponseValidator: ${it.status}")
            }
        }
        install(DefaultRequest) {
            url.takeFrom(BASE_URL)
        }
    }

    override fun close() {
        api.close()
    }
}


private val loggerPretty = object : Logger {
    private val BODY_START = "BODY START"
    private val BODY_END = "BODY END"
    private val LOG_NAME = "HTTP Client"
    override fun log(message: String) {
        if (!BuildConfig.DEBUG) return
        val startBody = message.indexOf(BODY_START)
        val endBody = message.indexOf(BODY_END)
        if (startBody != -1 && endBody != -1) {
            try {
                val header = message.substring(0, startBody)
                val jsonBody = message.substring(startBody + BODY_START.length, endBody)

                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(parseString(jsonBody))
                Log.i(LOG_NAME, "$header\n$prettyPrintJson")
            } catch (m: JsonSyntaxException) {
                Log.i(LOG_NAME, message)
            }
        } else {
            Log.e(LOG_NAME, message)
        }
    }
}