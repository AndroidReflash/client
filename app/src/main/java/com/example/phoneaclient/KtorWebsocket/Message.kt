package com.example.phoneaclient.KtorWebsocket

import com.example.phoneaclient.logic.dataClasses.SingleBackup
import kotlinx.serialization.Serializable

@Serializable
sealed class Message {
    @Serializable
    data class Scan(
        val singletonScan: String,
        val appName: String,
        val delay: String
    ) : Message()

    @Serializable
    data class RoomId(
        val id: Int,
        val command: String
    ) : Message()

    @Serializable
    data class Memory(val ram: List<String>): Message()

    @Serializable
    data class ScanResult(
        var directories: List<String> = emptyList(),
        val size: String = "",
        val time: String = "",
        val oldFileName: String = "",
        val scanDuration: String = "0",
        var packageName: String = ""
    ):Message()

    @Serializable
    data class BackupList(
        val listOfBackups: List<SingleBackup> = emptyList()
    ):Message()
    @Serializable
    data class CloseSession(val reason: String) : Message()
}

@Serializable
data class MessageContainer(
    val type: String,
    val payload: String
)