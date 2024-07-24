package com.example.phonebserver.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.phoneaclient.KtorWebsocket.WebSocketClient
import com.example.phoneaclient.logic.MainViewModel
import com.example.phoneaclient.logic.dataClasses.Host
import com.example.phoneaclient.logic.singletones.NavigationSingleTones
import kotlinx.coroutines.launch


@Composable
fun ConfigScreen(viewModel: MainViewModel, navController: NavController){
    var host by remember {
        mutableStateOf("")
    }
    var port by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        , contentAlignment = Alignment.Center){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Введите host сервера (например 0.0.0.0)", Modifier.padding(5.dp))
            TextField(value = host, onValueChange = {host = it}, Modifier.padding(5.dp))
            Text(text = "Введите порт сервера (например 8080)", Modifier.padding(5.dp))
            TextField(value = port, onValueChange = {port = it}, Modifier.padding(5.dp))
            Button(onClick = {
                if(host != "" && port != ""){
                    viewModel.updateIp(
                        Host(
                            host,
                            port.toInt()
                        )
                    )
                    navController.navigate(NavigationSingleTones.MainScreen.route)
                }else{
                    Toast.makeText(context, "Нужно заполнить все поля", Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "Сохранить")
            }
        }
    }
}