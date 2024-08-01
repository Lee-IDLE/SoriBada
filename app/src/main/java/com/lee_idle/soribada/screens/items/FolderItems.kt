package com.lee_idle.soribada.screens.items

import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.lee_idle.soribada.R
import com.lee_idle.soribada.models.FolderListData

@Composable
fun folderItems(thumbnail: Bitmap, musicData: FolderListData, onClicked: (Bitmap, FolderListData) -> Unit) {
    var isPlayed by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp)
            .fillMaxWidth()
            .clickable {

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

        // TODO: 노래 재생 관련 기능을 함수 타입으로 전달 받아 실행한다.
        if(musicData.artist.isNotEmpty()){
            // 오른쪽 재생 버튼
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
    val testData = FolderListData(
        title = "제목", albumID = 0L, fullPath = "/sdcard/Music/", id = 0,artist = "아티스트")
    folderItems(thumbnail = defaultThumbnail, testData)
}