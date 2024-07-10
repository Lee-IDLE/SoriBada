package com.lee_idle.soribada.viewModels

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee_idle.soribada.R
import com.lee_idle.soribada.SoriBadaApplication
import com.lee_idle.soribada.models.FolderListData
import com.lee_idle.soribada.models.MediaData
import kotlinx.coroutines.launch
import java.io.File

class FolderViewModel: ViewModel() {
    private val _folderList = MutableLiveData<List<FolderListData>>()
    val folderList: LiveData<List<FolderListData>>
        get() = _folderList

    private val _fileList = MutableLiveData<List<MediaData>>()
    val fileList: LiveData<List<MediaData>>
        get() = _fileList

    init{
        getListFromDirectory()
    }

    private fun getListFromDirectory(){
        viewModelScope.launch {
            // 경로: https://paulaner80.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EA%B2%BD%EB%A1%9C
            //SoriBadaApplication.instance.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            val path: String = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                val manager = SoriBadaApplication.instance.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                manager.primaryStorageVolume.directory?.absolutePath ?: ""
            } else {
                Environment.getExternalStorageDirectory().absolutePath
            }

            val directory = File("$path/Music")
            if (directory.exists() && directory.isDirectory) {
                val filesAndDirs = directory.listFiles()
                val list = mutableListOf<FolderListData>()
                filesAndDirs?.forEach {
                    if(it.isDirectory){
                        list.add(FolderListData(
                            image = "",
                            title = it.name,
                            album = "",
                            fullPath = it.path
                        ))
                    }else {
                        val mediaData = getFileData(it.absolutePath)
                        if (mediaData != null){
                            list.add(FolderListData(
                                image = mediaData.albumArtist,
                                title = mediaData.title,
                                album = mediaData.album,
                                fullPath = mediaData.path
                            ))
                        } else {
                            println("오류: MediaStore가 정보 가져오기 실패 - $it")
                        }
                    }
                    println("파일명: ${it.name}")
                }
                _folderList.value = list
            }

        }
    }

    /**
     * 전달 받은 경로가 음원파일이면 그 정보를 가져옵니다.
     * id, title, artist, album, duration, album_artist, data 
     * @param filePath
     * @return 값이 있으면 MediaData, 없으면 null 반환
     */
    fun getFileData(filePath: String): MediaData? {
        // 지정한 폴더와 그 아래에 있는 모든 음원 파일을 가져온다.
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        // 메인 저장소(내부 저장소) Android/media 가 경로다.
        val projection = arrayOf(
            MediaStore.Audio.Media._ID, // 파일 고유 ID
            MediaStore.Audio.Media.TITLE, // 음원 파일 제목
            MediaStore.Audio.Media.ARTIST, // 음원 파일의 아티스트
            MediaStore.Audio.Media.ALBUM, // 음원 파일 앨범
            MediaStore.Audio.Media.DURATION, // 음원 파일의 재생 시간(밀리초_
            MediaStore.Audio.Media.ALBUM_ARTIST, // 앨범 아트 이미지의 절대 경로)
            //MediaStore.Audio.Media.IS_FAVORITE, // 즐겨찾기 여부
            MediaStore.Audio.Media.DATA, // 음원 파일의 절대 경로
        )

        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE ?"

        val args: String = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val manager = SoriBadaApplication.instance.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            manager.primaryStorageVolume.directory?.absolutePath ?: ""
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        val selectionArgs = arrayOf("$args/Music/%")

        var cursor: Cursor? = null
        try{
            cursor = SoriBadaApplication.instance.contentResolver.query(
                listUrl,
                projection,
                selection,
                selectionArgs,
                null,
            )
        } catch (e: Exception){
            // TODO: Invalid column is_favorite 오류 발생 수정 필요
            println("오류: ${e.message}")
        }
        var fileItem: MediaData? = null
        if(cursor != null){
            while(cursor.moveToNext()) {
                val id = cursor.getColumnIndex(projection[0])
                val title = cursor.getColumnIndex(projection[1])
                val artist = cursor.getColumnIndex(projection[2])
                val album = cursor.getColumnIndex(projection[3])
                val duration = cursor.getColumnIndex(projection[4])
                val albumArtist = cursor.getColumnIndex(projection[5])
                //val isFavorite = cursor.getColumnIndex(projection[6])
                val path = cursor.getColumnIndex(projection[6])

                fileItem = MediaData(
                    id = if(id != -1) cursor.getString(id) else "",
                    title = if(title != -1) cursor.getString(title) else "",
                    artist = if(artist != -1) cursor.getString(artist) else "",
                    album = if(album != -1) cursor.getString(album) else "",
                    duration = if(duration != -1) cursor.getInt(duration).toUInt() else 0.toUInt(),
                    albumArtist = if(albumArtist != -1) cursor.getString(albumArtist) ?: "" else "",
                    isFavorite = false,//if(isFavorite != -1) cursor.getInt(isFavorite) == 1 else false,
                    path = if(path != -1) cursor.getString(path) else ""
                )
            }
        }
        return fileItem
    }
}