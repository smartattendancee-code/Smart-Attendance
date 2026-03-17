package uk.ac.tees.mad.smartattendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import uk.ac.tees.mad.smartattendance.data.Repo.RepoImpl
import uk.ac.tees.mad.smartattendance.presentation.AppViewModel

class AppViewModelFactory(
    private val backStackEntry: NavBackStackEntry
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AppViewModel(
            repo = RepoImpl(),

        ) as T
    }
}