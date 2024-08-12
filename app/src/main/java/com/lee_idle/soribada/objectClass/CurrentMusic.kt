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
    /* TODO:
    노래 정보에 Index를 넣는거야. 폴더에서 위 부터 아래로 1..n 까지 Index를 넣는거지.
    그리고 순서 재생 때는 이전 노래 하면 Index를 -1 시키고 다음 노래 하면 +1 시키는 거야.

    셔플일 때는 이전 노래들을 '덱'에 넣고 최대 크기를 50 정도로 설정한 다음
    (왼쪽 출력, 오른쪽 삽입 으로 했을 때)
    삽입한 노래의 갯수가 50을 넘어가려고 하면 왼쪽에서 출력시키고,
    이전 노래를 누르면 오른쪽에서 출력을 시키자.
     */
}