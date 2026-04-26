package uk.ac.tees.mad.smartattendance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.smartattendance.model.ResultState
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.ui.theme.*

@Composable
fun SettingScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val settingsState by viewModel.settingsState

    SettingContent(
        settingsState = settingsState,
        onLogout = {
            viewModel.logout()
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        },
        onClearLocal = {
            viewModel.clearLocalState()
        },
        onClearFirestore = {
            viewModel.clearFirestoreCache()
        }
    )
}

@Composable
fun SettingContent(
    settingsState: ResultState<String>,
    onLogout: () -> Unit,
    onClearLocal: () -> Unit,
    onClearFirestore: () -> Unit
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



            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClearFirestore,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryNavy
                )
            ) {
                Text(
                    text = "Clear Firestore Cache",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            when (settingsState) {

                is ResultState.Loading -> {
                    CircularProgressIndicator()
                }

                is ResultState.Succes -> {
                    if (settingsState.data.isNotEmpty()) {
                        Text(
                            text = settingsState.data,
                            color = PrimaryNavy
                        )
                    }
                }

                is ResultState.error -> {
                    Text(
                        text = settingsState.message,
                        color = ErrorRed
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingContent(
        settingsState = ResultState.Succes("Firestore cache cleared successfully"),
        onLogout = {},
        onClearLocal = {},
        onClearFirestore = {}
    )
}