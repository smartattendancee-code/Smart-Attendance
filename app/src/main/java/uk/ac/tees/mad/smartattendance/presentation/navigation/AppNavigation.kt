package uk.ac.tees.mad.smartattendance.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(route = NavRoutes.SPLASH) { backStackEntry ->
            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )
            SplashScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    }