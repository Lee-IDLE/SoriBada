package com.lee_idle.soribada.navBar

import androidx.compose.material.icons.Icons
import com.lee_idle.soribada.R
import com.lee_idle.soribada.SoriBadaApplication

object NavBarItems {
    val BarItem = listOf(
        BarItem(
            title = "Folder",
            image =  if(SoriBadaApplication.darkTheme.value!!)
            { R.drawable.ic_folder_white_24 } else { R.drawable.ic_folder_black_24 },
            route = "folder"
        ),
        BarItem(
            title = "Favorites",
            image =  if(SoriBadaApplication.darkTheme.value!!)
            { R.drawable.ic_favorite_border_white_24 } else { R.drawable.ic_favorite_border_black_24 },
            route = "favorites"
        ),
        BarItem(
            title = "Album",
            image =  if(SoriBadaApplication.darkTheme.value!!)
            { R.drawable.ic_photo_album_white_24 } else { R.drawable.ic_photo_album_black_24 },
            route = "album"
        ),
        BarItem(
            title = "Artist",
            image =  if(SoriBadaApplication.darkTheme.value!!)
            { R.drawable.ic_person_white_24 } else { R.drawable.ic_person_black_24 },
            route = "artist"
        ),
        BarItem(
            title = "Category",
            image =  if(SoriBadaApplication.darkTheme.value!!)
            { R.drawable.ic_category_white_24 } else { R.drawable.ic_category_black_24 },
            route = "category"
        )
    )
}