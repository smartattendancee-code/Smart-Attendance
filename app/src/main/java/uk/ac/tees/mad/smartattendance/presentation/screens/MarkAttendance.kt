package uk.ac.tees.mad.smartattendance.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkAttendanceScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val state = viewModel.attendanceState.value

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())

    var selectedDateMillis by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    val selectedDate = Date(selectedDateMillis)
    val selectedDateKey = formatter.format(selectedDate)
    val selectedDateDisplay = displayFormatter.format(selectedDate)

    val existingRecord = state.records.find { it.date == selectedDateKey }

    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var duplicateMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .padding(24.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "Mark Attendance",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryDarkNavy
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Picker Button
            Button(
                onClick = { showDatePicker = true },
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedDateDisplay,
                    color = PrimaryDarkNavy
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // If Attendance Already Marked
            if (existingRecord != null) {

                duplicateMessage = "Attendance already marked as ${existingRecord.status}"

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundCard)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = duplicateMessage!!,
                            color = if (existingRecord.status == "Present")
                                Color(0xFF4CAF50)
                            else ErrorRed,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back")
                        }
                    }
                }

            } else {

                // Selection Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    AttendanceOptionButton(
                        text = "Present",
                        selected = selectedStatus == "Present",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedStatus = "Present"
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    AttendanceOptionButton(
                        text = "Absent",
                        selected = selectedStatus == "Absent",
                        color = ErrorRed,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedStatus = "Absent"
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Preview
                if (selectedStatus != null) {
                    Text(
                        text = "Selected: $selectedStatus",
                        fontWeight = FontWeight.Medium,
                        color = PrimaryDarkNavy
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        selectedStatus?.let {
                            viewModel.markAttendance(selectedDateKey, it)
                            navController.popBackStack()
                        }
                    },
                    enabled = selectedStatus != null && !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryDarkNavy
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Save Attendance")
                    }
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error!!,
                        color = ErrorRed
                    )
                }
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDateMillis
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDateMillis =
                            datePickerState.selectedDateMillis
                                ?: System.currentTimeMillis()
                        selectedStatus = null
                        duplicateMessage = null
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}


@Composable
fun AttendanceOptionButton(
    text: String,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) color else BackgroundCard
        )
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else color,
            fontWeight = FontWeight.Bold
        )
    }
}
