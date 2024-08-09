package com.lee_idle.soribada.viewModels

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee_idle.soribada.SoriBadaApplication
import com.lee_idle.soribada.models.MusicData
import com.lee_idle.soribada.models.MediaData
import com.lee_idle.soribada.objectClass.BackFuntion
import kotlinx.coroutines.launch
import java.io.File

class FolderViewModel: ViewModel() {
    private val _folderList = MutableLiveData<List<MusicData>>()
    val folderList: LiveData<List<MusicData>>
        get() = _folderList

    private val _fileList = MutableLiveData<List<MediaData>>()
    val fileList: LiveData<List<MediaData>>
        get() = _fileList

    private var currentPath: String = ""

    init{
        // 경로: https://paulaner80.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EA%B2%BD%EB%A1%9C
        //SoriBadaApplication.instance.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val path: String = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val manager = SoriBadaApplication.instance.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            manager.primaryStorageVolume.directory?.absolutePath ?: ""
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        val defaultPath = "$path/Music"
        getListFromDirectory(defaultPath)

        BackFuntion.setBackTraceFuntion {
            getListFromDirectory(currentPath.substringBeforeLast("/"))
        }
    }

    fun getListFromDirectory(path: String){
        viewModelScope.launch {
            currentPath = path
            val directory = File(path)
            if (directory.exists() && directory.isDirectory) {
                val filesAndDirs = directory.listFiles()
                val tempFolderList = mutableListOf<MusicData>()
                val tempFileList = mutableListOf<MusicData>()

                filesAndDirs?.forEach {
                    if(it.isDirectory){
                        tempFolderList.add(MusicData(
                            id = 0L,
                            title = it.name,
                            artist = "",
                            albumID = 0L,
                            duration = 0,
                            albumArtist = "",
                            favorite = false,
                            path = it.path,
                        ))
                    }else {
                        val mediaData = getFileData(it.absolutePath)
                        if (mediaData != null){
                            tempFileList.add(MusicData(
                                id = mediaData.id.toLong(),
                                title = mediaData.title,
                                artist = mediaData.artist,
                                albumID = mediaData.albumID,
                                duration = mediaData.duration.toInt(),
                                albumArtist = mediaData.albumArtist,
                                favorite = false,
                                path = mediaData.path,
                            ))
                        } else {
                            println("오류: MediaStore가 정보 가져오기 실패 - $it")
                        }
                    }
                    println("파일명: ${it.name}")
                }
                _folderList.value = tempFolderList + tempFileList
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
        // MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI //filePath.substringBeforeLast("/").toUri()
        // 메인 저장소(내부 저장소) Android/media 가 경로다.
        val projection = arrayOf(
            MediaStore.Audio.Media._ID, // 파일 고유 ID
            MediaStore.Audio.Media.TITLE, // 음원 파일 제목
            MediaStore.Audio.Media.ARTIST, // 음원 파일의 아티스트
            MediaStore.Audio.Media.ALBUM_ARTIST, // 음원 파일 앨범
            MediaStore.Audio.Media.ALBUM_ID, // 앨범 아트 이미지의 절대 경로
            MediaStore.Audio.Media.DURATION, // 음원 파일의 재생 시간(밀리초_
            //MediaStore.Audio.Media.IS_FAVORITE, // 즐겨찾기 여부
            MediaStore.Audio.Media.DATA, // 음원 파일의 절대 경로
        )

        val selection = "${MediaStore.Files.FileColumns.DATA} = ?" // LIKE ?

        val selectionArgs = arrayOf(filePath) // 폴더 내 전부 검색시 필요: /%

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
                val path = cursor.getColumnIndex(projection[6])
                val cursorPath = cursor.getString(path)

                println("$cursorPath / $filePath")

                if (cursorPath.equals(filePath, ignoreCase = true)) {
                    val id = cursor.getColumnIndex(projection[0])
                    val title = cursor.getColumnIndex(projection[1])
                    val artist = cursor.getColumnIndex(projection[2])
                    val albumArtist = cursor.getColumnIndex(projection[3])
                    val albumID = cursor.getColumnIndex(projection[4])
                    val duration = cursor.getColumnIndex(projection[5])
                    //val isFavorite = cursor.getColumnIndex(projection[6])

                    fileItem = MediaData(
                        id = if(id != -1) cursor.getString(id) else "",
                        title = if(title != -1) cursor.getString(title) else "",
                        artist = if(artist != -1) cursor.getString(artist) else "",
                        albumID = if(albumID != -1) cursor.getLong(albumID) else -1L,
                        duration = if(duration != -1) cursor.getInt(duration).toUInt() else 0.toUInt(),
                        albumArtist = if(albumArtist != -1) cursor.getString(albumArtist) ?: "" else "",
                        isFavorite = false,//if(isFavorite != -1) cursor.getInt(isFavorite) == 1 else false,
                        path = if(path != -1) cursor.getString(path) ?: "" else ""
                    )
                    break
                }
            }
        }
        return fileItem
    }

    fun getThumbnailFromPath(fileUri: Uri): Bitmap? {
        val imageSize = Size(50, 50)
        var thumbnail: Bitmap? = null
        try {
            thumbnail = SoriBadaApplication.instance.contentResolver.loadThumbnail(fileUri, imageSize, null)
        }
        catch (e: Exception) {
            println("음원 썸네일 가져오기 실패: ${e.message}")
        }
        return thumbnail

        /*
        // API29 이전 버전에서는 ALBUM_ART를 사용해 썸네일을 가져온다
        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
        val selection = "${MediaStore.Audio.Albums._ID}=?"
        val selectionArgs = arrayOf(albumId.toString())


        val cursor: Cursor? = SoriBadaApplication.instance.contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            val albumArtColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)

            if (cursor.moveToFirst()) {
                val albumArtPath = cursor.getString(albumArtColumn)
                return BitmapFactory.decodeFile(albumArtPath)
            }
        }
        return null
         */
    }

    fun convertDrawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}