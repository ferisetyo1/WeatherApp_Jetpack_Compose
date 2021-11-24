package weather.codeid.feri.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import weather.codeid.feri.data.network.response.WeatherResponse
import weather.codeid.feri.utils.Constant

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") q: String,
        @Query("appid") key: String = Constant.API_KEY_WEATHERS,
        @Query("lang") lang:String="id"
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") key: String = Constant.API_KEY_WEATHERS,
        @Query("lang") lang:String="id"
    ): Response<WeatherResponse>
}