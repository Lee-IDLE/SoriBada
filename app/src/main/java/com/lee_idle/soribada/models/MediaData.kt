package com.lee_idle.soribada.models

data class MediaData(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val duration: UInt = 0.toUInt(),
    val albumArtist: String = "",
    val isFavorite: Boolean = false,
    val path: String = ""
)