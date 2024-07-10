package com.lee_idle.soribada.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.lee_idle.soribada.viewModels.FolderViewModel
import androidx.compose.runtime.remember as remember

@Composable
fun Folder() {
    //Text("폴더에요!")

    val folderViewModel = FolderViewModel()
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val folderList by folderViewModel.fileList.observeAsState()

    val currentFileList = remember { mutableListOf<String>() }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 5.dp)
    ) {

        if(folderViewModel.fileList.value != null){
            items(folderViewModel.fileList.value!!){ item ->
                Text(item.title)
            }
        }
         /*
        if(folderViewModel.testList.value != null){
            items(folderViewModel.testList.value!!){folderName ->
                Text(folderName)
            }
        }

          */
    }
}

/*
            val fileTree = File("./").walk()
                .maxDepth(1)
                .onEnter {file ->
                    println("enter $file")
                    true
                }
                .map {file ->
                    currentFileList.add(file.name)
                }

            items(currentFileList) {name ->
                Text(name)
            }

             */