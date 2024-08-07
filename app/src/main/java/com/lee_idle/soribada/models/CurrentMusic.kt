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

    private val _musicData: MutableLiveData<MusicData?> = MutableLiveData(null)
    val musicData: LiveData<MusicData?>
        get() = _musicData

    fun setMusicData(data: MusicData) {
        _musicData.value = data
    }

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

    // TODO: 정지 후 다시 시작시 처음부터 시작함 수정 필요
    private fun musicPlay() {
        mediaPlayer?.release() // 기존에 재생 중인 음악 해제
        mediaPlayer = MediaPlayer().apply{
            setDataSource(musicData.value?.path)
            prepare()
            start()
        }
    }

    fun musicPause() {
        mediaPlayer?.pause()
        musicPlayToggle()
    }
}