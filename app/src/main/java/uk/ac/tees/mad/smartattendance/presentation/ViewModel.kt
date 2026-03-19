package uk.ac.tees.mad.smartattendance.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uk.ac.tees.mad.smartattendance.domain.Repo
import uk.ac.tees.mad.smartattendance.model.AttendanceModel
import uk.ac.tees.mad.smartattendance.model.ResultState
import uk.ac.tees.mad.smartattendance.model.UserData

class AppViewModel(
    private val repo: Repo
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()



    private val _loginScreenState = mutableStateOf(LogInScreenState())
    val loginScreenState = _loginScreenState

    fun loginUser(userData: UserData) {
        viewModelScope.launch {
            repo.loginuserwithemailandpassword(userData).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _loginScreenState.value =
                            LogInScreenState(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _loginScreenState.value =
                            LogInScreenState(
                                success = true,
                                userdata = result.data
                            )
                    }

                    is ResultState.error -> {
                        _loginScreenState.value =
                            LogInScreenState(error = result.message)
                    }
                }
            }
        }
    }

    fun resetLoginState() {
        _loginScreenState.value = LogInScreenState()
    }


    private val _signupScreenState = mutableStateOf(SignUpScreenState())
    val signupScreenState = _signupScreenState

    fun registerUser(userData: UserData) {
        viewModelScope.launch {
            repo.registeruserwithemailandpassword(userData).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _signupScreenState.value =
                            SignUpScreenState(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _signupScreenState.value =
                            SignUpScreenState(
                                success = true,
                                userdata = result.data
                            )
                    }

                    is ResultState.error -> {
                        _signupScreenState.value =
                            SignUpScreenState(error = result.message)
                    }
                }
            }
        }
    }

    fun resetSignupState() {
        _signupScreenState.value = SignUpScreenState()
    }


    private val _attendanceState = mutableStateOf(AttendanceState())
    val attendanceState = _attendanceState

    private var attendanceJob: Job? = null


    fun observeAttendance() {

        attendanceJob?.cancel()

        attendanceJob = viewModelScope.launch {
            repo.observeAttendance().collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _attendanceState.value =
                            AttendanceState(isLoading = true)
                    }

                    is ResultState.Succes -> {

                        val list = result.data

                        val totalDays = list.size
                        val presentDays =
                            list.count { it.status == "Present" }
                        val absentDays =
                            list.count { it.status == "Absent" }

                        val percentage =
                            if (totalDays == 0) 0
                            else (presentDays * 100) / totalDays

                        _attendanceState.value =
                            AttendanceState(
                                records = list,
                                totalDays = totalDays,
                                presentDays = presentDays,
                                absentDays = absentDays,
                                percentage = percentage
                            )
                    }

                    is ResultState.error -> {
                        _attendanceState.value =
                            AttendanceState(error = result.message)
                    }
                }
            }
        }
    }


    fun markAttendance(date: String, status: String) {
        viewModelScope.launch {
            repo.markAttendance(date, status).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _attendanceState.value =
                            _attendanceState.value.copy(isLoading = true)
                    }

                    is ResultState.Succes -> {

                    }

                    is ResultState.error -> {
                        _attendanceState.value =
                            _attendanceState.value.copy(error = result.message)
                    }
                }
            }
        }
    }




    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun clearLocalState() {
        _attendanceState.value = AttendanceState()
    }
}




data class SignUpScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean = false
)

data class LogInScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean = false
)

data class AttendanceState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val records: List<AttendanceModel> = emptyList(),
    val totalDays: Int = 0,
    val presentDays: Int = 0,
    val absentDays: Int = 0,
    val percentage: Int = 0
)
