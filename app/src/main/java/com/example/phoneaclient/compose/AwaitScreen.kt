package com.example.phoneaclient.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneaclient.logic.MainViewModel

@Composable
fun AwaitScreen(viewModel: MainViewModel){
    val memoryList by viewModel.memory.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column {
            Text(text = "На сервере идёт процесс восстановления")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black)
            ){
                itemsIndexed(memoryList){ index, element->
                    Text(text = "Process $index: $element",
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp, end = 5.dp, bottom = 1.dp, top = 1.dp), fontSize = 13.sp)
                }
            }
        }
    }
}