package com.lee_idle.soribada.screens.items

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.lee_idle.soribada.R
import com.lee_idle.soribada.objectClass.CurrentMusic
import com.lee_idle.soribada.models.MusicData
import com.lee_idle.soribada.viewModels.FolderViewModel
import kotlin.random.Random

@Composable
fun folderItems(thumbnail: Bitmap, musicData: MusicData, folderViewModel: FolderViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp)
            .fillMaxWidth()
            .clickable {
                if(musicData.id == 0L){
                    // 폴더인 경우
                    folderViewModel.getListFromDirectory(musicData.path)
                } else {

                    if(CurrentMusic.musicData.value != null){
                        if(CurrentMusic.musicData.value!!.path.substringBeforeLast("/") !=
                            musicData.path.substringBeforeLast("/")){
                            CurrentMusic.currentMusicList.clear()
                            CurrentMusic.setCurrentMusicList(folderViewModel.fileList)

                            for (n in 0 until CurrentMusic.currentMusicList.size) {
                                val ranNum = Random.nextInt(CurrentMusic.currentMusicList.size)
                                CurrentMusic.addPlayListIndex(ranNum)
                            }
                        }
                    }


                    // 음원 파일인 경우
                    CurrentMusic.setTumbnail(thumbnail)
                    CurrentMusic.setMusicData(musicData)
                    CurrentMusic.musicPlay()
                }
            }
    ) {
        // 왼쪽 썸네일, 제목, 아티스트
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                bitmap = thumbnail.asImageBitmap(),
                contentDescription = "Thumbnail",
                modifier = Modifier.size(50.dp, 50.dp)
            )

            Column {
                // TODO: 제목이 길면 왼쪽으로 흐르는 애니메이션 효과 적용
                Text(
                    text = musicData.title,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                    fontSize = 17.sp
                )
                Text(
                    text = musicData.artist,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 12.sp
                )
            }
        }

        if(musicData.artist.isNotEmpty()){
            // 오른쪽 재생 버튼 TODO: 꼭 필요한지 재고
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier) {
                Button(
                    onClick = { /*TODO*/ },
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp, 40.dp),
                ){

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun viewTest() {
    val context = LocalContext.current
    val drawable = ContextCompat.getDrawable(context, R.drawable.ic_folder_white_24)

    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    val defaultThumbnail: Bitmap = drawable.toBitmap()
    val testData = MusicData(
        thumbnail = null,
        id = 0L,
        title = "테스트 제목",
        artist = "",
        albumID = 0L,
        duration = 0,
        albumArtist = "",
        favorite = false,
        path = "테스트 경로",
        index = 0,
    )

    val folderViewModel = FolderViewModel()
    folderItems(thumbnail = defaultThumbnail, testData, folderViewModel)
}