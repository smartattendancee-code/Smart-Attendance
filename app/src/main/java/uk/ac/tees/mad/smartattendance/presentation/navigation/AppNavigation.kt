package uk.ac.tees.mad.smartattendance.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.smartattendance.AppViewModelFactory
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.presentation.screens.HomeScreen
import uk.ac.tees.mad.smartattendance.presentation.screens.LoginScreen
import uk.ac.tees.mad.smartattendance.presentation.screens.MarkAttendanceScreen
import uk.ac.tees.mad.smartattendance.presentation.screens.SettingScreen
import uk.ac.tees.mad.smartattendance.presentation.screens.SignUpScreen
import uk.ac.tees.mad.smartattendance.presentation.screens.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val auth= FirebaseAuth.getInstance()

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
        composable(route = NavRoutes.HOME) { backStackEntry ->
            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(route = NavRoutes.MARK_ATTENDANCE) { backStackEntry ->
            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )
            MarkAttendanceScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(route = NavRoutes.SETTINGS) { backStackEntry ->
            val viewModel: AppViewModel = viewModel(
                factory = AppViewModelFactory(backStackEntry)
            )
            SettingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


    }

    }