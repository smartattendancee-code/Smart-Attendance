package uk.ac.tees.mad.smartattendance.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.smartattendance.AppViewModelFactory
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.presentation.screens.LoginScreen
import uk.ac.tees.mad.smartattendance.presentation.screens.SignUpScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {
//        composable(route = NavRoutes.SPLASH) { backStackEntry ->
//            val viewModel: AppViewModel = viewModel(
//                factory = AppViewModelFactory(backStackEntry)
//            )
//            SplashScreen(
//                navController = navController,
//                viewModel = viewModel
//            )
//        }
        composable(route = NavRoutes.LOGIN) { backStackEntry ->
            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(route = NavRoutes.SIGNUP) { backStackEntry ->
            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )
            SignUpScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    }