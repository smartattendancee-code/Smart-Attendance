package uk.ac.tees.mad.smartattendance.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.smartattendance.R
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.presentation.AttendanceState
import uk.ac.tees.mad.smartattendance.presentation.navigation.NavRoutes
import uk.ac.tees.mad.smartattendance.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    navController: NavController
) {
    val state = viewModel.attendanceState.value
    

    LaunchedEffect(Unit) {
        viewModel.observeAttendance()
    }

    val currentDate = remember { Date() }
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    
    val todayDateKey = formatter.format(currentDate)
    val todayDateDisplay = displayFormatter.format(currentDate)


    val todayRecord = state.records.find { it.date == todayDateKey }
    val todayStatus = todayRecord?.status ?: "Not Marked"
    
    HomeContent(
        date = todayDateDisplay,
        status = todayStatus,
        attendanceState = state,
        onMarkAttendance = {
            viewModel.markAttendance(todayDateKey, "Present")
        },
        onLogout = {
            viewModel.logout()
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        }
    )
}

@Composable
fun HomeContent(
    date: String,
    status: String,
    attendanceState: AttendanceState,
    onMarkAttendance: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, User",
                        style = MaterialTheme.typography.headlineSmall,
                        color = PrimaryDarkNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

            // Today's Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Today's Attendance",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = status,
                        style = MaterialTheme.typography.headlineMedium,
                        color = when(status) {
                            "Present" -> Color(0xFF4CAF50) // Green
                            "Absent" -> ErrorRed
                            else -> PrimaryNavy
                        },
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = onMarkAttendance,
                        enabled = status == "Not Marked" && !attendanceState.isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryDarkNavy,
                            disabledContainerColor = Color.Gray
                        )
                    ) {
                        if (attendanceState.isLoading) {
                             CircularProgressIndicator(
                                color = TextWhite,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(text = if (status == "Not Marked") "Mark Present" else "Marked")
                        }
                    }
                    
                    if (attendanceState.error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = attendanceState.error,
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Statistics
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryDarkNavy,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    title = "Total Days",
                    value = attendanceState.totalDays.toString(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatCard(
                    title = "Percentage",
                    value = "${attendanceState.percentage}%",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    title = "Present",
                    value = attendanceState.presentDays.toString(),
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatCard(
                    title = "Absent",
                    value = attendanceState.absentDays.toString(),
                    color = ErrorRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color = PrimaryNavy
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeContent(
        date = "Wednesday, 12 Oct 2023",
        status = "Not Marked",
        attendanceState = AttendanceState(
            totalDays = 20,
            presentDays = 18,
            absentDays = 2,
            percentage = 90
        ),
        onMarkAttendance = {},
        onLogout = {}
    )
}
