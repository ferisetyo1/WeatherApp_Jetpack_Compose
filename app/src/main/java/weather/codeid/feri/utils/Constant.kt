package weather.codeid.feri.utils

object Constant {
    const val BASE_URL_API = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY_WEATHERS = "0012a18b367daf42bcb21b702e0fcc3c"

    fun getImageOpenWeatherMap(s:String)="http://openweathermap.org/img/w/$s.png"

    object Route {
        const val homeScreen = "homeScreen"
        const val splashScreen = "splashScreen"
        const val loginScreen = "loginScreen"
        const val registerScreen = "registerScreen"
        const val profilScreen = "profilScreen"
        const val searchScreen = "searchScreen"
    }
}