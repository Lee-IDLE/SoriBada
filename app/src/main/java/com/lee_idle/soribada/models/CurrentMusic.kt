package com.lee_idle.soribada.models

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CurrentMusic {
    var thumbnail: Bitmap? = null
    var musicData: MusicData? = null
    private var _isPlayed = MutableLiveData(false)
    val isPlayed: LiveData<Boolean>
        get()= _isPlayed

    private var mediaPlayer: MediaPlayer? = null
    fun musicPlayToggle() {
        if(_isPlayed.value == true) {
            _isPlayed.value = false
        } else {
            _isPlayed.value = true
            musicPlay()
        }
    }

    private fun musicPlay() {
        mediaPlayer?.release() // 기존에 재생 중인 음악 해제
        mediaPlayer = MediaPlayer().apply{
            setDataSource(musicData?.path)
            prepare()
            start()
        }
    }
}