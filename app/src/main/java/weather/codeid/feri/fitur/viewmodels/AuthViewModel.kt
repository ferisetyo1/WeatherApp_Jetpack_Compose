package weather.codeid.feri.fitur.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import weather.codeid.feri.data.local.DataStoreManager
import weather.codeid.feri.data.local.roomdb.entity.User
import weather.codeid.feri.data.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dataStoreManager: DataStoreManager
) :
    ViewModel() {
    val isSuccess = mutableStateOf(false)
    val msgError = mutableStateOf("")
    val msgSuccess = mutableStateOf("")
    val email = dataStoreManager.email
    val user = mutableStateOf<User?>(null)

    fun getUser(){
        viewModelScope.launch {
            user.value = weatherRepository.getUser(dataStoreManager.email.firstOrNull().orEmpty())
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            msgError.value = ""
            isSuccess.value = weatherRepository.register(user = user)
            if (!isSuccess.value) {
                msgError.value = "Email telah digunakan"
            } else dataStoreManager.editEmail(user.email.orEmpty())
        }
    }

    fun signin(email: String, password: String) {
        viewModelScope.launch {
            val user = weatherRepository.sign(email, password)
            isSuccess.value = user != null
            if (user == null) msgError.value = "Email/Password salah."
            else dataStoreManager.editEmail(user.email.orEmpty())
        }
    }

    fun updateProfil(user: User) {
        viewModelScope.launch {
            val success = weatherRepository.updateProfil(user)
            if (success) {
                msgSuccess.value = "Berhasil mengupdate profil"
            }
        }
    }
}