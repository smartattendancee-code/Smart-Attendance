package uk.ac.tees.mad.smartattendance.domain

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.smartattendance.model.ResultState
import uk.ac.tees.mad.smartattendance.model.UserData

interface Repo {
    fun registeruserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
    fun loginuserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
}