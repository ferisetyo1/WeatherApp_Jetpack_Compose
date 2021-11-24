package weather.codeid.feri.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.http.GET
import retrofit2.http.Query
import weather.codeid.feri.data.local.roomdb.dao.UserDao
import weather.codeid.feri.data.local.roomdb.entity.User
import weather.codeid.feri.data.network.WeatherApi
import weather.codeid.feri.data.network.response.WeatherResponse
import weather.codeid.feri.utils.Constant
import weather.codeid.feri.utils.Resource
import weather.codeid.feri.utils.isNetworkAvailable
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val userDao: UserDao,
    private val api: WeatherApi
) {
    suspend fun register(user: User): Boolean {
        return try {
            userDao.register(user)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun sign(email: String, password: String): User? {
        return try {
            userDao.signin(email, password)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getUser(email: String): User? {
        return try {
            userDao.getUser(email)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateProfil(user: User): Boolean {
        return try {
            userDao.updateProfil(user)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getWeather(
        q: String
    ): Resource<WeatherResponse> {
        if (!isNetworkAvailable(appContext)) return Resource.Error("Tidak ada sambungan internet")
        val result = try {
            api.getWeather(q = q)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Terjadi kesalahan internal")
        }
        if (result.code()==200)return Resource.Success(result.body()!!)
        if (result.code()==404)return Resource.Success(WeatherResponse(message = "Kota tidak ditemukan"))
        return Resource.Error("Terjadi kesalahan internal")
    }

    suspend fun getWeather(
        lat: String,
        lon: String,
    ): Resource<WeatherResponse> {
        if (!isNetworkAvailable(appContext)) return Resource.Error("Tidak ada sambungan internet")
        val result = try {
            api.getWeather(lat = lat, lon = lon)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Terjadi kesalahan internal")
        }
        if (result.code()==200)return Resource.Success(result.body()!!)
        if (result.code()==404)return Resource.Success(WeatherResponse(message = "Kota tidak ditemukan"))
        return Resource.Error("Terjadi kesalahan internal")
    }
}