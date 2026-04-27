package uk.ac.tees.mad.smartattendance.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel
import uk.ac.tees.mad.smartattendance.presentation.AttendanceState
import uk.ac.tees.mad.smartattendance.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkAttendanceScreen(
    viewModel: AppViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val state = viewModel.attendanceState.value

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())

    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    val selectedDate = selectedDateMillis?.let { Date(it) }
    val selectedDateKey = selectedDate?.let { formatter.format(it) }
    val selectedDateDisplay = selectedDate?.let { displayFormatter.format(it) }

    val existingRecord = selectedDateKey?.let {
        state.records.find { record -> record.date == it }
    }

    LaunchedEffect(Unit) {
        viewModel.observeAttendance()
    }

    LaunchedEffect(selectedDateKey, existingRecord) {
        if (selectedDateKey != null && existingRecord != null) {
            Toast.makeText(
                context,
                "Attendance already marked for this date",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(state.error) {
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
        }
    }

    MarkAttendanceContent(
        state = state,
        selectedDateDisplay = selectedDateDisplay,
        selectedDateKey = selectedDateKey,
        existingRecordStatus = existingRecord?.status,
        selectedStatus = selectedStatus,
        onSelectDate = { showDatePicker = true },
        onSelectStatus = { selectedStatus = it },
        onSave = {
            if (selectedStatus != null && selectedDateKey != null) {
                viewModel.markAttendance(selectedDateKey, selectedStatus!!)
                navController.popBackStack()
            }
        },
        showDatePicker = showDatePicker,
        onDismissDatePicker = { showDatePicker = false },
        onConfirmDate = { millis ->
            selectedDateMillis = millis
            selectedStatus = null
            showDatePicker = false
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkAttendanceContent(
    state: AttendanceState,
    selectedDateDisplay: String?,
    selectedDateKey: String?,
    existingRecordStatus: String?,
    selectedStatus: String?,
    onSelectDate: () -> Unit,
    onSelectStatus: (String) -> Unit,
    onSave: () -> Unit,
    showDatePicker: Boolean,
    onDismissDatePicker: () -> Unit,
    onConfirmDate: (Long?) -> Unit
) {

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

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onSelectDate,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundCard)
            ) {
                Text(
                    text = selectedDateDisplay ?: "Select Date",
                    color = PrimaryDarkNavy
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedDateKey != null && existingRecordStatus != null) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundCard)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Attendance already marked as $existingRecordStatus",
                            color = if (existingRecordStatus == "Present")
                                Color(0xFF4CAF50)
                            else ErrorRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            } else if (selectedDateKey != null) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    AttendanceOptionButton(
                        text = "Present",
                        selected = selectedStatus == "Present",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    ) { onSelectStatus("Present") }

                    Spacer(modifier = Modifier.width(16.dp))

                    AttendanceOptionButton(
                        text = "Absent",
                        selected = selectedStatus == "Absent",
                        color = ErrorRed,
                        modifier = Modifier.weight(1f)
                    ) { onSelectStatus("Absent") }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSave,
                    enabled = selectedStatus != null && !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDarkNavy)
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
            }
        }

        if (showDatePicker) {

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = System.currentTimeMillis()
            )

            DatePickerDialog(
                onDismissRequest = onDismissDatePicker,
                confirmButton = {
                    TextButton(onClick = {
                        onConfirmDate(datePickerState.selectedDateMillis)
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDatePicker) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors()
                )
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

@Preview(showBackground = true)
@Composable
fun MarkAttendancePreview() {
    MarkAttendanceContent(
        state = AttendanceState(),
        selectedDateDisplay = "Monday, 12 Feb 2026",
        selectedDateKey = "2026-02-12",
        existingRecordStatus = null,
        selectedStatus = "Present",
        onSelectDate = {},
        onSelectStatus = {},
        onSave = {},
        showDatePicker = false,
        onDismissDatePicker = {},
        onConfirmDate = {}
    )
}