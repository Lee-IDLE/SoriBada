package com.lee_idle.soribada.screens.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lee_idle.soribada.R
import com.lee_idle.soribada.objectClass.CurrentMusic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round

@Composable
fun CurrentMusicUI(){
    var progressValue by remember { mutableStateOf(0F) }
    val currentMusicThumbnail by CurrentMusic.thumbnail.observeAsState()
    val currentMusicData by CurrentMusic.musicData.observeAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .aspectRatio(10F / 1.3F)
    ){
        LinearProgressIndicator(
            progress = progressValue,
            modifier = Modifier.fillMaxWidth()
        )

        LaunchedEffect(Unit){
            coroutineScope.launch{
                while(CurrentMusic.isPlayed.value!!){
                    val progress = CurrentMusic.mediaPlayer?.currentPosition!!.toFloat() / CurrentMusic.mediaPlayer!!.duration.toFloat()
                    progressValue = round(progress*100) / 100
                    delay(1000)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Row(
                modifier = Modifier.weight(5F)
            ){
                if(currentMusicThumbnail != null){
                    Image(
                        bitmap = currentMusicThumbnail!!.asImageBitmap(),
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1F / 1F)
                            .padding(3.dp)
                    )
                }

                Text(
                    text = currentMusicData!!.title,
                    fontSize = 20.sp,
                    maxLines = 1
                    )
            }

            Row(
                modifier = Modifier.weight(3F)
            ){
                // previouse button
                IconButton(
                    onClick = {
                              CurrentMusic.prevMusic()
                              },
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_skip_previous_white_24),
                        contentDescription = "Skip Previous Button Image",
                        modifier = Modifier.size(40.dp, 40.dp)
                    )
                }

                // play or pause button
                IconButton(
                    onClick = {
                        if(CurrentMusic.isPlayed.value!!){
                            CurrentMusic.musicPause()
                        }else{
                            CurrentMusic.musicPlay()
                        }
                              },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_play_circle_white_24),
                        contentDescription = "Play Button Image",
                        modifier = Modifier.size(40.dp, 40.dp)
                    )
                }

                // next button
                IconButton(
                    onClick = {
                              CurrentMusic.nextMusic()
                              },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_skip_next_white_24),
                        contentDescription = "Skip Next Button Image",
                        modifier = Modifier.size(40.dp, 40.dp)
                    )
                }
            }
        }
    }
}