package com.lee_idle.soribada.models

import android.graphics.Bitmap

data class MusicData(
    val thumbnail: Bitmap?,
    val id: Long,
    val title: String,
    val artist: String,
    val albumID: Long,
    val duration: Int,
    val albumArtist: String,
    val favorite: Boolean,
    val path: String,
    val index: Int
)
