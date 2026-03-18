package uk.ac.tees.mad.smartattendance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.smartattendance.R
import uk.ac.tees.mad.smartattendance.model.UserData
import uk.ac.tees.mad.smartattendance.presentation.navigation.NavRoutes
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val state = viewModel.loginScreenState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Navigate on success
    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.LOGIN) { inclusive = true }
            }
            viewModel.resetLoginState()
        }
    }

    LoginContent(
        email = email,
        onEmailChange = {
            email = it
            errorMessage = ""
        },
        password = password,
        onPasswordChange = {
            password = it
            errorMessage = ""
        },
        isLoading = state.isLoading,
        errorMessage = state.error ?: errorMessage,
        onLoginClick = {
            if (email.isBlank() || password.isBlank()) {
                errorMessage = "All fields are required"
            } else {
                viewModel.loginUser(
                    UserData(email = email, password = password)
                )
            }
        },
        onSignUpClick = {
            navController.navigate(NavRoutes.SIGNUP)
        }
    )
}

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundCard
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Attendance App",
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryDarkNavy
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Secure access to your account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy,
                        focusedLabelColor = PrimaryNavy,
                        cursorColor = PrimaryNavy
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy,
                        focusedLabelColor = PrimaryNavy,
                        cursorColor = PrimaryNavy
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage,
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLoginClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryDarkNavy,
                        contentColor = TextWhite
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = TextWhite,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Login")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onSignUpClick) {
                    Text(
                        "Don't have an account? Sign Up",
                        color = PrimaryNavy
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        isLoading = false,
        errorMessage = null,
        onLoginClick = {},
        onSignUpClick = {}
    )
}
