package weather.codeid.feri.data.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import weather.codeid.feri.data.local.roomdb.dao.UserDao
import weather.codeid.feri.data.local.roomdb.entity.User

@Database(entities = [User::class], version = 3)
abstract class WeatherDB : RoomDatabase() {
    abstract fun userDao(): UserDao
}