package com.example.phoneaclient.KtorWebsocket

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.phoneaclient.logic.MainViewModel
import com.example.phoneaclient.logic.singletones.SingletonesForActivating
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.EOFException
import java.time.Instant

class WebSocketClient(
    private val viewModel: MainViewModel
) {
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    private var webSocketSession: WebSocketSession? = null
    private val sessionMutex = Mutex()

    suspend fun connect() {
        Log.d("ScanConnect", Instant.now().toString())
        viewModel.viewModelScope.launch {
            try {
                Log.d(
                    "WebSocket",
                    "Attempting to connect to ws://${viewModel.ip.value.host}:${viewModel.ip.value.port}/ws"
                )
                client.webSocket(
                    method = HttpMethod.Get,
                    host = viewModel.ip.value.host,
                    port = viewModel.ip.value.port,
                    path = "/ws"
                ) {
                    sessionMutex.withLock {
                        webSocketSession = this
                        viewModel.webSocketSession = webSocketSession
                        Log.d("ScanWebSocketSession", "webSocketSession initialized: $webSocketSession")
                    }
                    Log.d("WebSocket", "Connected to WebSocket server")

                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Binary -> TODO()
                                is Frame.Close -> TODO()
                                is Frame.Ping -> TODO()
                                is Frame.Pong -> TODO()
                                is Frame.Text -> {
                                    val text = frame.readText()
                                    Log.d("WebSocket", text)
                                    val messageContainer = Json.decodeFromString<MessageContainer>(text)
                                    val message: Message = when (messageContainer.type) {
                                        "Scan" ->
                                            Json.decodeFromString<Message.Scan>(
                                                messageContainer.payload
                                            )
                                        "RoomId" ->
                                            Json.decodeFromString<Message.RoomId>(
                                                messageContainer.payload
                                            )
                                        "Memory" ->
                                            Json.decodeFromString<Message.Memory>(
                                                messageContainer.payload
                                            )
                                        "ScanResult" ->
                                            Json.decodeFromString<Message.ScanResult>(
                                                messageContainer.payload
                                            )
                                        "BackupList" ->
                                            Json.decodeFromString<Message.BackupList>(
                                                messageContainer.payload
                                            )
                                        "CloseSession" ->
                                            Json.decodeFromString<Message.CloseSession>(
                                                messageContainer.payload
                                            )
                                        else -> throw IllegalArgumentException("Unknown message type")
                                    }
                                    when (message) {
                                        is Message.Scan -> {

                                        }
                                        is Message.RoomId -> {

                                        }
                                        is Message.Memory -> {
                                            viewModel.updateMemory(message.ram)
                                            Log.d("WebSocket", "${message.ram}")
                                        }
                                        is Message.ScanResult -> {
                                            viewModel.loadScanResult(message)
                                            Log.d("WebSocket", "$message")
                                        }
                                        is Message.BackupList -> {
                                            viewModel.updateListOfSingleBackup(message.listOfBackups)
                                            Log.d("WebSocketBackup", "$message")
                                        }
                                        is Message.CloseSession -> {
                                            Log.d("WebSocketClient", "Received CloseSession message")
                                            webSocketSession?.close(CloseReason(CloseReason.Codes.NORMAL, message.reason))
                                            webSocketSession = null
                                            viewModel.webSocketSession = null
                                            Log.d("WebSocketClient", "WebSocket session closed on client")
                                        }
                                        else -> Log.d("WebSocketClient", "Unsupported frame type: ${frame.frameType}")

                                    }
                                }
                            }
                        }

                    } catch (e: ClosedReceiveChannelException) {
                        Log.d("WebSocket", "Connection closed: ${e.message}")
                    } catch (e: Throwable) {
                        Log.d("WebSocketErr", "Error: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("WebSocket", "Failed to connect: ${e.message}")
                e.printStackTrace()
            } catch (e: EOFException){
                Log.d("WebSocket", "Connection broken, refresh it")
            }
        }
    }

    fun restore(id: Int, command: String, webSocketSession: WebSocketSession?){
        runBlocking {
            val messageRoom = Message.RoomId(
                id,
                command
            )
            val containerRoom = MessageContainer("RoomId", Json.encodeToString(messageRoom))
            webSocketSession?.send(Frame.Text(Json.encodeToString(containerRoom)))
        }
    }


    fun scanStart(webSocketSession: WebSocketSession?){
        viewModel.viewModelScope.launch {
            try {
                val messageA = Message.Scan(
                    SingletonesForActivating.StartScan.value,
                    viewModel.inputValues.value.appName,
                    viewModel.inputValues.value.delay
                )
                val containerA = MessageContainer("Scan", Json.encodeToString(messageA))
                webSocketSession?.send(Frame.Text(Json.encodeToString(containerA)))
            }catch (e: EOFException){
                Log.d("WebSocket", "Connection broken, refresh it")
                viewModel.webSocketSession = null
            }
        }
    }

    fun scanStop(webSocketSession: WebSocketSession?){
        viewModel.viewModelScope.launch {
            try {
                val messageA = Message.Scan(
                    SingletonesForActivating.StopScan.value,
                    viewModel.inputValues.value.appName,
                    viewModel.inputValues.value.delay
                )
                val containerA = MessageContainer("Scan", Json.encodeToString(messageA))
                webSocketSession?.send(Frame.Text(Json.encodeToString(containerA)))
            }catch (e: EOFException){
                Log.d("WebSocket", "Connection broken, refresh it")
                viewModel.webSocketSession = null
            }
        }
    }

}