package com.example.phoneaclient.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneaclient.logic.FileTreeValues
import com.example.phoneaclient.logic.MainViewModel
import com.example.phoneaclient.logic.SingletonesForChecking
import com.example.phoneaclient.ui.theme.changed
import com.example.phoneaclient.ui.theme.newFiles
import com.example.phoneaclient.ui.theme.same
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FileTree(
    viewModel: MainViewModel
){
    val scanResult = viewModel.scanResult.collectAsState()
    var pack by remember {
        mutableStateOf("")
    }
    pack = scanResult.value.packageName
    val fileTree = FileTreeValues()
    var m1 by remember {
        mutableStateOf(0)
    }
    var mMax by remember {
        mutableStateOf(0)
    }
    val memList = viewModel.memory.collectAsState()
    memList.value.forEach {
        m1 += it[0].code
        mMax += it[1].code
    }
    val scanTime = scanResult.value.scanDuration.substringBefore(".")
    val date = fileTree.date + scanResult.value.time
    val dur = fileTree.durationOfScan + scanTime + fileTree.millis
    val overallSize = fileTree.sizeOfDirectories + scanResult.value.size.substringBefore("/")
    val list = listOf(date, dur, overallSize)
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
        ) {
            items(list){
                Text(text = it, Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 1.dp, top = 1.dp), fontSize = 15.sp)
            }

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Black)
        ){
            itemsIndexed(viewModel.memory.value){ index, element->
                Text(text = "Process $index: $element", Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 1.dp, top = 1.dp), fontSize = 13.sp)
            }
        }
        LazyColumn {
            items(scanResult.value.directories){
                if(it.contains(regex = Regex(SingletonesForChecking.ScanChanged.value))){
                    TreeElement(text = it.substringBefore(" "), color = changed)
                }else if(it.contains(regex = Regex(SingletonesForChecking.ScanNew.value))){
                    TreeElement(text = it.substringBefore(" "), color = newFiles)
                }else{
                    TreeElement(text = it, color = same)
                }
            }
        }
    }
}

@Composable
fun TreeElement(text: String, color: Color){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .border(2.dp, Color.Gray)
        .background(color = color)){
        Text(text = text, Modifier.padding(5.dp))
    }
}
