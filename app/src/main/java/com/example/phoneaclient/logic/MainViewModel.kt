package com.example.phoneaclient.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.phoneaclient.KtorWebsocket.Message
import com.example.phoneaclient.logic.dataClasses.Host
import com.example.phoneaclient.logic.dataClasses.InputValues
import com.example.phoneaclient.logic.dataClasses.SingleBackup
import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.ref.WeakReference

class MainViewModel : ViewModel() {

    private val _delay = MutableStateFlow("")
    val delay: StateFlow<String> = _delay

    fun updateDelay(newValue: String){
        _delay.value = newValue
    }

    private val _appName = MutableStateFlow("")
    val appName: StateFlow<String> = _appName

    fun updateAppName(newValue: String){
        _appName.value = newValue
    }

    private val _listOfBackup = MutableStateFlow<List<SingleBackup>>(emptyList())
    val listOfBackup: StateFlow<List<SingleBackup>> = _listOfBackup

    fun updateListOfSingleBackup(newValue: List<SingleBackup>){
        _listOfBackup.value = newValue
    }

    private var _webSocketSession: WeakReference<WebSocketSession>? = null

    var webSocketSession: WebSocketSession?
        get() = _webSocketSession?.get()
        set(value) {
            _webSocketSession = if (value != null) WeakReference(value) else null
        }

    private val _inputValues = MutableStateFlow(InputValues())
    val inputValues: StateFlow<InputValues> = _inputValues

    fun updateInputValue(newValue: InputValues){
        _inputValues.value = newValue
    }

    private val _ip = MutableStateFlow(Host())
    val ip: StateFlow<Host> = _ip

    fun updateIp(newValue: Host){
        _ip.value = newValue
    }

    private val _memory = MutableStateFlow<List<String>>(emptyList())
    val memory: StateFlow<List<String>> = _memory.asStateFlow()

    fun updateMemory(newValue: List<String>){
        _memory.value = newValue
    }

    private val _scanResult = MutableStateFlow(Message.ScanResult())
    val scanResult: StateFlow<Message.ScanResult> = _scanResult.asStateFlow()

    fun loadScanResult(newValue: Message.ScanResult) {
        _scanResult.value = newValue
    }
}