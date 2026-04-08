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

import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.ui.theme.*

@Composable
fun SignUpScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val state = viewModel.signupScreenState.value

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Navigate on success
    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SIGNUP) { inclusive = true }
                popUpTo(NavRoutes.LOGIN) { inclusive = true }
            }
            // Reset state? ViewModel doesn't have resetSignUpState but maybe Good to have.
        }
    }

    SignUpContent(
        name = name,
        onNameChange = {
            name = it
            errorMessage = ""
        },
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
        onSignUpClick = {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                errorMessage = "All fields are required"
            } else {
                viewModel.registerUser(
                    UserData(name = name, email = email, password = password)
                )
            }
        },
        onLoginClick = {
            navController.navigate(NavRoutes.LOGIN)
        }
    )
}

@Composable
fun SignUpContent(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit
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
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryDarkNavy
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Sign up to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy,
                        focusedLabelColor = PrimaryNavy,
                        cursorColor = PrimaryNavy
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    onClick = onSignUpClick,
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
                        Text("Sign Up")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onLoginClick) {
                    Text(
                        "Already have an account? Login",
                        color = PrimaryNavy
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpContent(
        name = "",
        onNameChange = {},
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        isLoading = false,
        errorMessage = null,
        onSignUpClick = {},
        onLoginClick = {}
    )
}
