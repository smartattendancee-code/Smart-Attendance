package uk.ac.tees.mad.smartattendance.presentation.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.smartattendance.R
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel

import uk.ac.tees.mad.smartattendance.ui.theme.BackgroundMain
import uk.ac.tees.mad.smartattendance.ui.theme.PrimaryDarkNavy

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AppViewModel
) {

    LaunchedEffect(Unit) {

        delay(1500) // 1.5 second splash delay

        if (viewModel.isUserLoggedIn()) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
        }
    }

    SplashContent()
}


@Composable
fun SplashContent() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "App Logo"
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                color = PrimaryDarkNavy
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashContent()
}
