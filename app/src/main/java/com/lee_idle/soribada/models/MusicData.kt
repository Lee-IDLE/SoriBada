package com.lee_idle.soribada.models

data class MusicData(
    val id: Long,
    val title: String,
    val artist: String,
    val albumID: Long,
    val duration: Int,
    val albumArtist: String,
    val favorite: Boolean,
    val path: String,
)
