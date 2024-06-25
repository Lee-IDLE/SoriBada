package com.lee_idle.soribada

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
        // 30 이상 버전

        setContent {
            val mainViewModel = MainViewModel()
            BaseView(darkThemeState = SoriBadaApplication.darkTheme.value!!,
                viewModel = mainViewModel)
        }
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
            topBar = {  },
            content = { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    MainScreen(viewModel, navController)
                } },
            bottomBar = { BottomNavigationBar(viewModel, navController) }
        )
    }
}

@Composable
fun TopAppBar(viewModel: MainViewModel) {


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
            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        NavigationBar{
            val backStackEntry by navcontroller.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            NavBarItems.BarItem.forEach{navItem ->
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navcontroller.navigate(navItem.route){
                            popUpTo(navcontroller.graph.findStartDestination().id){
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

        var sumDistance = 0f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(40f)
                .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
                .background(color = Color.Black, shape = RoundedCornerShape(15.dp))
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState {distance ->
                        viewModel.GoToHome()
                        /*
                        sumDistance += distance
                        if(sumDistance > 100){
                            sumDistance = 0f

                        }

                         */
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