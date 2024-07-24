package com.example.phoneaclient.logic.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class SingleBackup(
    val id: Int = 0,
    val backupName: String = "",
    val archiveUri: String = "",
    val textUri: String = ""
)

