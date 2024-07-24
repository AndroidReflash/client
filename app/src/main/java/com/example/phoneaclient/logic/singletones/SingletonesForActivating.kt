package com.example.phoneaclient.logic.singletones

sealed class SingletonesForActivating(val value: String) {
    object StartScan: SingletonesForActivating(value = "PhoneBServerStartScan")
    object StopScan: SingletonesForActivating(value = "PhoneBServerStopScan")
    object Restore: SingletonesForActivating(value = "PhoneBServerRestore")
}