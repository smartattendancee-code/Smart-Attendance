package uk.ac.tees.mad.smartattendance.domain

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.smartattendance.model.AttendanceModel
import uk.ac.tees.mad.smartattendance.model.ResultState
import uk.ac.tees.mad.smartattendance.model.UserData

interface Repo {
    fun registeruserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
    fun loginuserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
    fun markAttendance(date: String, status: String): Flow<ResultState<String>>

    fun observeAttendance(): Flow<ResultState<List<AttendanceModel>>>

}