package uk.ac.tees.mad.smartattendance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.presentation.AttendanceState
import uk.ac.tees.mad.smartattendance.ui.theme.*

@Composable
fun SettingScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    SettingContent(
        onLogout = {
            viewModel.logout()
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        },
        onClearCache = {
            viewModel.clearLocalState()
        }
    )
}


@Composable
fun SettingContent(
    onLogout: () -> Unit,
    onClearCache: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryDarkNavy
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Clear Cache Button
            Button(
                onClick = onClearCache,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackgroundCard
                )
            ) {
                Text(
                    text = "Clear Local Cache",
                    color = PrimaryDarkNavy,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErrorRed
                )
            ) {
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingContent(
        onLogout = {},
        onClearCache = {}
    )
}
