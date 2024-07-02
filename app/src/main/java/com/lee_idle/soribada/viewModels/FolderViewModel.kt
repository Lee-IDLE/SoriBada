package com.lee_idle.soribada.viewModels

import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee_idle.soribada.SoriBadaApplication
import com.lee_idle.soribada.models.MediaData
import kotlinx.coroutines.launch

class FolderViewModel: ViewModel() {
    private val _folderList = MutableLiveData<List<MediaData>>()
    val folderList: LiveData<List<MediaData>>
        get() = _folderList

    init{
        getListFromDirectory()
    }

    private fun getListFromDirectory(){
        viewModelScope.launch {
            val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID, // 파일 고유 ID
                MediaStore.Audio.Media.TITLE, // 음원 파일 제목
                MediaStore.Audio.Media.ARTIST, // 음원 파일의 아티스트
                MediaStore.Audio.Media.ALBUM, // 음원 파일 앨범
                MediaStore.Audio.Media.DURATION, // 음원 파일의 재생 시간(밀리초_
                MediaStore.Audio.Media.ALBUM_ARTIST, // 앨범 아트 이미지의 절대 경로)
                MediaStore.Audio.Media.IS_FAVORITE, // 즐겨찾기 여부
                MediaStore.Audio.Media.DATA, // 음원 파일의 절대 경로
            )

            var cursor: Cursor? = null
            try{
                cursor = SoriBadaApplication.instance.contentResolver.query(
                    listUrl,
                    projection,
                    null,
                    null,
                    null,
                )
            } catch (e: Exception){
                // TODO: Invalid column is_favorite 오류 발생 수정 필요
                println("오류: ${e.message}")
            }

            if(cursor != null){
                val fileList = mutableListOf<MediaData>()
                while(cursor.moveToNext()) {
                    val id = cursor.getColumnIndex(projection[0])
                    val title = cursor.getColumnIndex(projection[1])
                    val artist = cursor.getColumnIndex(projection[2])
                    val album = cursor.getColumnIndex(projection[3])
                    val duration = cursor.getColumnIndex(projection[4])
                    val albumArtist = cursor.getColumnIndex(projection[5])
                    val isFavorite = cursor.getColumnIndex(projection[6])
                    val path = cursor.getColumnIndex(projection[7])

                    fileList.add(MediaData(
                        id = if(id != -1) cursor.getString(id) else "",
                        title = if(title != -1) cursor.getString(title) else "",
                        artist = if(artist != -1) cursor.getString(artist) else "",
                        album = if(album != -1) cursor.getString(album) else "",
                        duration = if(duration != -1) cursor.getInt(duration).toUInt() else 0.toUInt(),
                        albumArtist = if(albumArtist != -1) cursor.getString(albumArtist) else "",
                        isFavorite = if(isFavorite != -1) cursor.getInt(isFavorite) == 1 else false,
                        path = if(path != -1) cursor.getString(path) else ""
                    ))
                }

                _folderList.value = fileList
            }
        }
    }
}