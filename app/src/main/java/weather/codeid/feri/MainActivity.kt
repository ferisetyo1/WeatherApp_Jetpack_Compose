package weather.codeid.feri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import weather.codeid.feri.fitur.ui.home.homeScreen
import weather.codeid.feri.fitur.ui.login.loginScreen
import weather.codeid.feri.fitur.ui.profil.profilScreen
import weather.codeid.feri.fitur.ui.register.registerScreen
import weather.codeid.feri.fitur.ui.search.searchScreen
import weather.codeid.feri.fitur.ui.splash.splashScreen
import weather.codeid.feri.ui.theme.WeatherAppTheme
import weather.codeid.feri.utils.Constant

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Constant.Route.splashScreen,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    }
                ) {
                    composable(route = Constant.Route.splashScreen) {
                        splashScreen(navController = navController)
                    }
                    composable(route = Constant.Route.homeScreen) {
                        homeScreen(navController = navController)
                    }
                    composable(route = Constant.Route.registerScreen) {
                        registerScreen(navController = navController)
                    }
                    composable(route = Constant.Route.loginScreen) {
                        loginScreen(navController = navController)
                    }
                    composable(route = Constant.Route.profilScreen) {
                        profilScreen(navController = navController)
                    }
                    composable(route = Constant.Route.searchScreen) {
                        searchScreen(navController = navController)
                    }
                }
            }
        }
    }
}