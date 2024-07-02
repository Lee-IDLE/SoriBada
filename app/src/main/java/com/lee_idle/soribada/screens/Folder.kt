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

@Composable
fun Folder() {
    //Text("폴더에요!")

    val folderViewModel = FolderViewModel()
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val folderList by folderViewModel.folderList.observeAsState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 5.dp)
    ) {
        if(folderViewModel.folderList.value != null){
            items(folderViewModel.folderList.value!!){item ->
                Text(item.title)
            }
        }
    }
}