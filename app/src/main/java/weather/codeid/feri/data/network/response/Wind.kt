package weather.codeid.feri.data.network.response


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    val deg: Double? = null,
    @SerializedName("speed")
    val speed: Double? = null
)