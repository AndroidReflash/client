package com.example.phoneaclient.compose


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.phoneaclient.KtorWebsocket.WebSocketClient
import com.example.phoneaclient.logic.MainViewModel
import com.example.phoneaclient.logic.singletones.SingletonesForActivating
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanListResultsTree(viewModel: MainViewModel){
    val backups by viewModel.listOfBackup.collectAsState()
    var showAwaitScreen by remember { mutableStateOf(false) }
    var shouldHideAwaitScreen by remember { mutableStateOf(false) }

    // When showAwaitScreen true, launch timer for hiding
    LaunchedEffect(showAwaitScreen) {
        if (showAwaitScreen) {
            delay(3000) // Wait for 3 sec
            shouldHideAwaitScreen = true // Hiding screen
            delay(100) // Extra delay for guarantee of hiding
            showAwaitScreen = false // Refresh state for hide of AwaitScreen
        }
    }

    if (showAwaitScreen) {
        AwaitScreen(viewModel = viewModel)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 60.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 60.dp
                    )
            ) {
                items(backups) { backup ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .border(3.dp, color = Color.Black)
                            .clickable {

                                Log.d("eke", "${backup.archiveUri}")
                                Log.d("eke", "${backup.id}")

                                // Launch of fun restore and showing up AwaitScreen
                                val id = backup.id
                                val webSocketClient = WebSocketClient(viewModel)
                                viewModel.viewModelScope.launch {
                                    webSocketClient.restore(
                                        id,
                                        SingletonesForActivating.Restore.value,
                                        viewModel.webSocketSession
                                    )
                                    showAwaitScreen = true // Show AwaitScreen
                                }
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(text = backup.backupName, Modifier.padding(5.dp))
                    }
                }
            }
        }
    }
}
