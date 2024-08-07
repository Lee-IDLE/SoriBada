package com.lee_idle.soribada.screens.items

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lee_idle.soribada.R
import com.lee_idle.soribada.models.CurrentMusic

@Composable
fun CurrentMusicUI(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Row{
            if(CurrentMusic.thumbnail != null){
                Image(
                    bitmap = CurrentMusic.thumbnail!!.asImageBitmap(),
                    contentDescription = "Thumbnail",
                    modifier = Modifier.size(50.dp, 50.dp)
                )
            }

            Text(text = CurrentMusic.musicData.value!!.title)
        }

        Row{
            // previouse button
            IconButton(
                onClick = { /*TODO*/ },
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_skip_previous_white_24),
                    contentDescription = "Skip Previous Button Image",
                    modifier = Modifier.size(40.dp, 40.dp)
                )
            }

            // play or pause button
            IconButton(
                onClick = { CurrentMusic.musicPause() },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_play_circle_white_24),
                    contentDescription = "Play Button Image",
                    modifier = Modifier.size(40.dp, 40.dp)
                )
            }

            // next button
            IconButton(
                onClick = { /*TODO*/ },
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