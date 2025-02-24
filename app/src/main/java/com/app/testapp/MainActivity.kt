package com.app.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.testapp.utils.AppModule
import com.app.testapp.ui.filmDetailScreen.FilmDetailScreen
import com.app.testapp.ui.filmDetailScreen.FilmDetailViewModelFactory
import com.app.testapp.ui.filmListScreen.FilmListScreen
import com.app.testapp.ui.filmListScreen.FilmListViewModel
import com.app.testapp.ui.filmListScreen.FilmListViewModelFactory
import com.app.testapp.ui.route.Route
import com.app.testapp.ui.theme.TestAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.FilmList.path) {
        composable(Route.FilmList.path) {
            FilmListScreen(
                viewModel = viewModel<FilmListViewModel>(
                    factory = FilmListViewModelFactory(
                        AppModule.filmRepository
                    )
                ),
                onFilmClick = { filmId ->
                    navController.navigate(
                        Route.FilmDetail.path.replace("{filmId}", filmId.toString())
                    )
                }
            )
        }

        composable(
            route = Route.FilmDetail.path,
            arguments = listOf(navArgument("filmId") { type = NavType.IntType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId") ?: 0
            FilmDetailScreen(
                filmId = filmId,
                viewModel = viewModel(factory = FilmDetailViewModelFactory(AppModule.filmRepository)),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}