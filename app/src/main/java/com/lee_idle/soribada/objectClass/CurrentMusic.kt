package com.lee_idle.soribada.objectClass

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lee_idle.soribada.models.MusicData

object CurrentMusic {
    // 썸네일
    private val _thumbnail: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val thumbnail: LiveData<Bitmap?>
        get() = _thumbnail

    fun setTumbnail(thumbnail: Bitmap) {
        _thumbnail.value = thumbnail
    }

    // 현재 실행중인 음악의 정보
    private val _musicData: MutableLiveData<MusicData?> = MutableLiveData(null)
    val musicData: LiveData<MusicData?>
        get() = _musicData

    fun setMusicData(data: MusicData) {
        _musicData.value = data
        _mediaPlayer?.release() // 기존에 재생 중인 음악 해제
        _mediaPlayer = null
    }
    
    // 실행 여부
    private var _isPlayed = MutableLiveData(false)
    val isPlayed: LiveData<Boolean>
        get()= _isPlayed

    private var _mediaPlayer: MediaPlayer? = null
    val mediaPlayer: MediaPlayer?
        get() = _mediaPlayer

    // 플레이 순서 목록
    private val _playListOrder: ArrayList<Int> = ArrayList()
    private var currentIndex = 0;

    private fun currentIndexInc() {
        currentIndex++

        if (currentIndex >= _playListOrder.size)
            currentIndex = 0
    }

    private fun currentIndexDec() {
        currentIndex--

        if (currentIndex < 0)
            currentIndex = _playListOrder.size - 1
    }

    fun addPlayListIndex(item: Int) {
        _playListOrder.add(item)
    }

    fun clearPlayListOrder() {
        _playListOrder.clear()
    }

    // 현재 플레이 리스트에 있는 음악 목록
    private var _currentMusicList = mutableListOf<MusicData>()
    val currentMusicList: MutableList<MusicData>
        get() = _currentMusicList

    fun setCurrentMusicList(list: List<MusicData>){
        _currentMusicList = list.toMutableList()
    }
    fun addCurrentMusicList(item: MusicData) {
        _currentMusicList.add(item)
    }

    fun clearCurrentMusicList() {
        _currentMusicList.clear()
    }

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

    fun nextMusic() {
        currentIndexInc()
        _currentMusicList[currentIndex].let {
            /* TODO:
                현재 Folder에 (썸네일, 음악 정보) 이렇게 따로 있는데 해당 음악 Row를 선택하면 거기서
                썸네일과 음악 정보를 꺼내와 음악을 실행하고 있다.
                그런데 문제는, 현재 실행중인 음악(CurrentMusic)에서 다음 노래를 실행할 때 다음 노래의
                음악 정보는 가져왔지만 썸네일 정보가 없다!
                따라서 구조를 변경할 필요가 있다.
                CurrentMusic에 있는 currentMusicList에(MusicData 타입이다) 노래 정보가 있으니까
                MusicData타입에 썸네일을 넣어야 할 것 같다.
             */
            // 음원 파일인 경우
            //setTumbnail(it)
            setMusicData(it)
            musicPlay()
        }
    }

    fun prevMusic() {
        currentIndexDec()
    }
}