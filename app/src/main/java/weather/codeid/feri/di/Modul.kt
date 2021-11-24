package weather.codeid.feri.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import weather.codeid.feri.BuildConfig
import weather.codeid.feri.data.local.DataStoreManager
import weather.codeid.feri.data.local.roomdb.WeatherDB
import weather.codeid.feri.data.local.roomdb.dao.UserDao
import weather.codeid.feri.data.network.WeatherApi
import weather.codeid.feri.data.repository.WeatherRepository
import weather.codeid.feri.utils.Constant
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Modul {
    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        val httpCacheDirectory = File(context.cacheDir, "responses")

        try {
            val cache = Cache(
                httpCacheDirectory,
                10 * 1024 * 1024
            )
            httpClient.cache(cache)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (BuildConfig.DEBUG) {
            httpClient
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideLegionRepository(
        @ApplicationContext appContext: Context,
        userDao: UserDao,
        api: WeatherApi
    ) = WeatherRepository(appContext,userDao,api)

    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext appContext: Context): DataStoreManager =
        DataStoreManager(appContext)

    @Singleton
    @Provides
    fun weatherDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, WeatherDB::class.java, "weather_app_db").build()

    @Singleton
    @Provides
    fun userDao(roomdb: WeatherDB)=roomdb.userDao()

    @Singleton
    @Provides
    fun provideWeatherApi(client: OkHttpClient): WeatherApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.BASE_URL_API)
            .client(client)
            .build()
            .create(WeatherApi::class.java)
    }
}