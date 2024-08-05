package com.lee_idle.soribada

import android.graphics.Bitmap
import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lee_idle.soribada.models.CurrentMusic
import com.lee_idle.soribada.models.MusicData
import com.lee_idle.soribada.navBar.NavBarItems
import com.lee_idle.soribada.screens.Album
import com.lee_idle.soribada.screens.Artist
import com.lee_idle.soribada.screens.Category
import com.lee_idle.soribada.screens.Favorites
import com.lee_idle.soribada.screens.Folder
import com.lee_idle.soribada.ui.theme.SoriBadaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://developer.android.com/training/system-ui?hl=ko 시스템 UI 제어하기
        enableEdgeToEdge() // 전체 앱의 콘텐츠에 인셋을 적용(시스템 UI와 겹침)

        setContent {
            val mainViewModel = MainViewModel()
            BaseView(darkThemeState = SoriBadaApplication.darkTheme.value!!,
                viewModel = mainViewModel)
        }
    }

    override fun onResume() {
        super.onResume()

        if(Build.VERSION.SDK_INT >= 30){
            val windowsInsetsController = window.insetsController
            windowsInsetsController?.show(WindowInsets.Type.systemBars())
        } else {
            // Full Screen 관련 https://soda1127.github.io/deep-dive-in-android-full-screen-1/ 참고
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    override fun onPause() {
        super.onPause()

        // 애니메이션
        /*
        if(Build.VERSION.SDK_INT >= 34){
            overrideActivityTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom, 0)
        } else {
            overridePendingTransition(R.anim.none, R.anim.slide_out_bottom)
        }
         */
    }
}

@Composable
fun BaseView(
    darkThemeState: Boolean,
    viewModel: MainViewModel,
){
    val navController = rememberNavController()
    SoriBadaTheme(darkTheme = darkThemeState) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            topBar = { TopAppBar(viewModel, navController) },
            content = { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    MainScreen(viewModel, navController)
                } },
            bottomBar = { BottomNavigationBar(viewModel, navController) }
        )
    }
}

@Composable
fun TopAppBar(viewModel: MainViewModel, navController: NavHostController) {
    var title = ""
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    when(currentBackStackEntry?.destination?.route){
        "folder" -> title = "폴더"
        "favorites" -> title = "즐겨찾기"
        "album" -> title = "앨범"
        "artist" -> title = "아티스트"
        "category" -> title = "카테고리"
        else -> {  }
    }

    Spacer(modifier = Modifier.padding(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // https://developer.android.com/develop/ui/compose/components/button?hl=ko 버튼에 관하여
        Row(verticalAlignment = Alignment.CenterVertically){
            TextButton(
                onClick = {
                    if(navController.graph.findStartDestination() != currentBackStackEntry?.destination){
                        navController.popBackStack()
                    }

                    //navController.navigate(currentBackStackEntry?.destination?.route!!)
                    /*
                    navController.navigate(currentBackStackEntry?.destination?.route!!){
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }

                    }
                     */
                },
                modifier = Modifier.background(Color.Transparent)
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back_white_24),
                    contentDescription = "뒤로가기"
                )
            }

            Text(text = title)
        }

        Row{
            TextButton(
                onClick = {
                    /* TODO: 검색 화면 만들기 */
                },
                modifier = Modifier.background(Color.Transparent)
            ){
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search_white_24),
                    contentDescription = "검색")
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Folder.route
    ){
        composable(NavRoutes.Folder.route){
            Folder()
        }

        composable(NavRoutes.Favorites.route){
            Favorites()
        }

        composable(NavRoutes.Album.route){
            Album()
        }

        composable(NavRoutes.Artist.route){
            Artist()
        }

        composable(NavRoutes.Category.route){
            Category()
        }

    }
}

@Composable
fun BottomNavigationBar(viewModel: MainViewModel, navcontroller: NavHostController){
    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        val backColor = if(SoriBadaApplication.darkTheme.value!!) Color.White else Color.Black

        if(CurrentMusic.isPlayed.value == true) {


            Spacer(modifier = Modifier.padding(5.dp))
        }



        NavigationBar(
            modifier = Modifier.clip(shape = RoundedCornerShape(30.dp))
        ){
            val backStackEntry by navcontroller.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            NavBarItems.BarItem.forEach{navItem ->
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navcontroller.navigate(navItem.route){
                            popUpTo(navcontroller.graph.id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Image(imageVector = ImageVector
                            .vectorResource(id = navItem.image),
                            contentDescription = navItem.title)
                    })
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))

        // 화면의 가로 길이를 구해 1/3 길이로 만들면 될 듯 함
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Box(
            modifier = Modifier
                .width(screenWidth / 2)
                .align(Alignment.CenterHorizontally)
                .aspectRatio(40f)
                .background(
                    color = backColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { distance ->
                        viewModel.GoToHome()
                    }
                )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    SoriBadaTheme {

    }
}


/*
    [240801] TODO:
    1. 노래 실행 구조 변경
    - MainActivity에서 CompositionLocal을 사용해 폴더, 취향, 아티스트 등에 현재 실행 노래 정보(썸네일, 파일정보)
    등을 변경하는 람다를 전달하고, 노래를 눌러 실행하면 해당 정보들을 변경하도록 한다.
    - 현재 FolderListData라는 이름으로 노래 정보(Model)을 구현해 놨는데 이름 바꾸고, FolderViewModel에서
    가져오는 노래 정보들 모두 저장하도록 변경
 */