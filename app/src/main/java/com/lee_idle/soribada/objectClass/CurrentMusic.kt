package com.lee_idle.soribada.objectClass

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lee_idle.soribada.models.MusicData

object CurrentMusic {
    private val _thumbnail: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val thumbnail: LiveData<Bitmap?>
        get() = _thumbnail

    fun setTumbnail(thumbnail: Bitmap) {
        _thumbnail.value = thumbnail
    }

    private val _musicData: MutableLiveData<MusicData?> = MutableLiveData(null)
    val musicData: LiveData<MusicData?>
        get() = _musicData

    fun setMusicData(data: MusicData) {
        _musicData.value = data
        _mediaPlayer?.release() // 기존에 재생 중인 음악 해제
        _mediaPlayer = null
    }

    private var _isPlayed = MutableLiveData(false)
    val isPlayed: LiveData<Boolean>
        get()= _isPlayed

    private var _mediaPlayer: MediaPlayer? = null
    public val mediaPlayer: MediaPlayer?
        get() = _mediaPlayer

    fun musicPlay() {
        /*
        _mediaPlayer 값이 있는데 노래 재생을 하는 경우는 정지 후 다시 재생하는 경우다.
        _mediaPlayer 값이 없는데 노래 재생을 하는 경우 해당 노래를 처음으로 재생하는 경우다.
         */
        if(_mediaPlayer != null){
            _mediaPlayer?.start()
        } else {
            _mediaPlayer = MediaPlayer().apply{
                setDataSource(musicData.value?.path)
                prepare()
                start()
            }
        }

        _isPlayed.value = true
    }

    fun musicPause() {
        _mediaPlayer?.pause()
        _isPlayed.value = false
    }
}