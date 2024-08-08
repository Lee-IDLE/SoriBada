package com.lee_idle.soribada.screens

import android.content.ContentUris
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.lee_idle.soribada.R
import com.lee_idle.soribada.models.MusicData
import com.lee_idle.soribada.screens.items.folderItems
import com.lee_idle.soribada.viewModels.FolderViewModel
import java.io.File
import androidx.compose.runtime.remember as remember

@Composable
fun Folder() {
    val folderViewModel = FolderViewModel()
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val fileList by folderViewModel.folderList.observeAsState()

    val currentFileList = remember { folderViewModel.folderList }

    val context = LocalContext.current

    // 폴더 기본 이미지
    val folderDrawable = ContextCompat.getDrawable(context, R.drawable.ic_folder_white_24)
    folderDrawable?.let { folderViewModel.convertDrawableToBitmap(it) }

    val folderThumbnail: Bitmap? = folderDrawable?.toBitmap()

    // 노래 기본 이미지
    val defaultDrawable = ContextCompat.getDrawable(context, R.drawable.ic_music_note_white_24)
    defaultDrawable?.let { folderViewModel.convertDrawableToBitmap(it) }

    val defaultThumbnail: Bitmap? = defaultDrawable?.toBitmap()

    // 하나하나 다 버튼으로 만들어야 하나?
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 5.dp)
    ) {
        items(fileList!!){ item ->
            var fileThumbnail: Bitmap? = null
            if(item.albumID != 0L){
                val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, item.albumID)
                fileThumbnail = folderViewModel.getThumbnailFromPath(contentUri)
            }

            var sendThumbnail: Bitmap? = null

            val file = File(item.path)
            if(file.isDirectory){
                sendThumbnail = folderThumbnail // 폴더인 경우
            } else if (fileThumbnail == null) {
                sendThumbnail = defaultThumbnail // album 이미지가 없는 경우
            } else {
                sendThumbnail = fileThumbnail // album 이미지가 있는 경우
            }

            folderItems(
                thumbnail = sendThumbnail!!,
                musicData = item
            )
        }
    }
}

@Composable
fun listItem(thumbnail: Bitmap, item: MusicData){
    Row{
        Image(bitmap = thumbnail.asImageBitmap(), contentDescription = "thumbnail")
    }
}

@Preview(showBackground = true)
@Composable
fun FolderPreview() {

}