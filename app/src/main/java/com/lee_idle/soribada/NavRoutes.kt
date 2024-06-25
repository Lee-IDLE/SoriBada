package com.lee_idle.soribada

sealed class NavRoutes(val route: String) {
    object Folder: NavRoutes("folder")
    object Favorites: NavRoutes("favorites")
    object Album: NavRoutes("album")
    object Artist: NavRoutes("artist")
    object Category: NavRoutes("category")
}