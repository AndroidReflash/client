package com.example.phoneaclient.logic

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.phoneaclient.KtorWebsocket.WebSocketClient
import com.example.phoneaclient.interfaces.MainActivityLogicInterface
import com.example.phoneaclient.logic.singletones.NavigationSingleTones
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityLogic(
   private val viewModel: MainViewModel,
   private val navController: NavController
): MainActivityLogicInterface {

    override fun config() {
        navController.navigate(NavigationSingleTones.ConfigScreen.route)
    }

    override fun start() {
        val webSocketClient = WebSocketClient(viewModel)
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                webSocketClient.connect()
            }
            //to prevent start of the scanStart before connect
            while (viewModel.webSocketSession == null) {
                delay(100)
            }
            webSocketClient.scanStart(webSocketSession = viewModel.webSocketSession)
        }
    }

    override fun stop() {
        val webSocketClient = WebSocketClient(viewModel)
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                webSocketClient.connect()
            }
            //to prevent start of the scanStart before connect
            while (viewModel.webSocketSession == null) {
                delay(100)
            }
            webSocketClient.scanStop(webSocketSession = viewModel.webSocketSession)
        }
    }

    override fun scanList() {
        navController.navigate(NavigationSingleTones.ScanListScreen.route)
    }
}