package weather.codeid.feri.fitur.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import weather.codeid.feri.data.local.DataStoreManager
import weather.codeid.feri.data.local.roomdb.entity.User
import weather.codeid.feri.data.network.response.WeatherResponse
import weather.codeid.feri.data.repository.WeatherRepository
import weather.codeid.feri.utils.Resource
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dataStoreManager: DataStoreManager
) :
    ViewModel() {
    val isLoading = mutableStateOf(false)
    val msgError = mutableStateOf("")
    val email = dataStoreManager.email
    val user = mutableStateOf<User?>(null)
    val dataWeather = mutableStateOf<WeatherResponse?>(null)

    init {
        viewModelScope.launch {
            user.value = weatherRepository.getUser(dataStoreManager.email.firstOrNull().orEmpty())
        }
    }

    fun getWeather(lat: String, lon: String) {
        viewModelScope.launch {
            isLoading.value = true
            msgError.value = ""
            when (val result = weatherRepository.getWeather(lat, lon)) {
                is Resource.Success -> {
                    isLoading.value = false
                    result.data?.let {
                        dataWeather.value = it
                    }
                }
                is Resource.Error -> {
                    isLoading.value = false
                    msgError.value = result.message.orEmpty()
                }
            }
        }
    }

    private var searchJob: Job? = null

    fun search(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            isLoading.value = true
            msgError.value = ""
            delay(3000)
            when (val result = weatherRepository.getWeather(searchText)) {
                is Resource.Success -> {
                    isLoading.value = false
                    result.data?.let {
                        dataWeather.value = it
                    }
                }
                is Resource.Error -> {
                    isLoading.value = false
                    msgError.value = result.message.orEmpty()
                }
            }
        }
    }
}