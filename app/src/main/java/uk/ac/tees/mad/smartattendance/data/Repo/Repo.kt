package uk.ac.tees.mad.smartattendance.data.Repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uk.ac.tees.mad.smartattendance.domain.Repo
import uk.ac.tees.mad.smartattendance.model.AttendanceModel
import uk.ac.tees.mad.smartattendance.model.ResultState
import uk.ac.tees.mad.smartattendance.model.UserData

class RepoImpl : Repo {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    // ---------------- AUTH ----------------

    override fun registeruserwithemailandpassword(
        userdata: UserData
    ): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val email = userdata.email
        val password = userdata.password

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            trySend(ResultState.error("Email or Password cannot be empty"))
            close()
            return@callbackFlow
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Succes("Registration Successful"))
                } else {
                    trySend(
                        ResultState.error(
                            task.exception?.localizedMessage ?: "Unknown error"
                        )
                    )
                }
            }

        awaitClose { close() }
    }

    override fun loginuserwithemailandpassword(
        userdata: UserData
    ): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val email = userdata.email
        val password = userdata.password

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            trySend(ResultState.error("Email or Password cannot be empty"))
            close()
            return@callbackFlow
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Succes("Login Successful"))
                } else {
                    trySend(
                        ResultState.error(
                            task.exception?.localizedMessage ?: "Unknown error"
                        )
                    )
                }
            }

        awaitClose { close() }
    }
    override fun markAttendance(
        date: String,
        status: String
    ): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val userId = auth.currentUser?.uid

        if (userId == null) {
            trySend(ResultState.error("User not logged in"))
            close()
            return@callbackFlow
        }

        val attendanceRef = firestore
            .collection("attendance")
            .document(userId)
            .collection("records")
            .document(date)

        attendanceRef.get().addOnSuccessListener { document ->

            if (document.exists()) {
                trySend(ResultState.error("Attendance already marked for today"))
            } else {

                val attendance = AttendanceModel(date, status)

                attendanceRef.set(attendance)
                    .addOnSuccessListener {
                        trySend(ResultState.Succes("Attendance Marked"))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.error(it.localizedMessage ?: "Error"))
                    }
            }
        }

        awaitClose { close() }
    }


    // ---------------- REALTIME OBSERVER ----------------

    override fun observeAttendance():
            Flow<ResultState<List<AttendanceModel>>> = callbackFlow {

        trySend(ResultState.Loading)

        val userId = auth.currentUser?.uid

        if (userId == null) {
            trySend(ResultState.error("User not logged in"))
            close()
            return@callbackFlow
        }

        val registration: ListenerRegistration =
            firestore.collection("attendance")
                .document(userId)
                .collection("records")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        trySend(ResultState.error(error.localizedMessage ?: "Error"))
                        return@addSnapshotListener
                    }

                    val list = snapshot?.documents?.mapNotNull {
                        it.toObject(AttendanceModel::class.java)
                    } ?: emptyList()

                    trySend(ResultState.Succes(list))
                }

        awaitClose { registration.remove() }
    }
}