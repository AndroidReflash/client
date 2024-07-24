package com.example.phoneaclient.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.phoneaclient.logic.MainActivityLogic
import com.example.phoneaclient.logic.MainActivityValuesForUI
import com.example.phoneaclient.logic.MainViewModel
import com.example.phoneaclient.logic.dataClasses.InputValues

@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavController) {
    val fontSize = 12.sp
    var isScanning by remember {
        mutableStateOf(false)
    }
    val ui = MainActivityValuesForUI()
    val main = MainActivityLogic(viewModel, navController)
    val delay = remember { mutableStateOf(viewModel.delay.value) }
    val appName = remember { mutableStateOf(viewModel.appName.value) }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                        TextField(
                            modifier = Modifier.width(200.dp),
                            value = appName.value,
                            onValueChange = { newText ->
                                appName.value = newText
                                viewModel.updateAppName(appName.value)
                            }
                        )
                        Text(
                            text = ui.typeName,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                        TextField(
                            modifier = Modifier.width(100.dp),
                            value = delay.value,
                            onValueChange = { newText ->
                                delay.value = newText
                                viewModel.updateDelay(delay.value)
                            }
                        )
                        Text(
                            text = ui.typeDelay,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = ui.filesOfApp + appName.value,
                )
                Spacer(modifier = Modifier.height(3.dp))
                FileTree(viewModel = viewModel)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(5.dp),
            Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Button(onClick = {
                main.config()
            }) {
                Text(text = ui.config, fontSize = fontSize)
            }
            Button(onClick = {
                if(appName.value != "" && delay.value != ""){
                    viewModel.updateInputValue(
                        InputValues(
                            appName.value,
                            delay.value
                        )
                    )
                    main.start()
                }
            }) {
                Text(text = ui.start, fontSize = fontSize)
            }
            Button(onClick = {
                main.stop()
                isScanning = false
            }) {
                Text(text = ui.stop, fontSize = fontSize)
            }
            Button(onClick = {
                main.scanList()
            }) {
                Text(text = ui.scanList, fontSize = fontSize)
            }
        }
    }
}