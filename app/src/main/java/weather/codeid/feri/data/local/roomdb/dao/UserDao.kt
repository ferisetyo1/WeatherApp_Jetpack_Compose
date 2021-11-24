package weather.codeid.feri.data.local.roomdb.dao

import androidx.room.*
import weather.codeid.feri.data.local.roomdb.entity.User

@Dao
interface UserDao {

    @Query("select * from user where email = :email and password = :password limit 1")
    suspend fun signin(email: String, password: String): User

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: User)

    @Update
    suspend fun updateProfil(user: User)

    @Query("select * from user where email = :email  limit 1")
    suspend fun getUser(email: String): User

    @Query("select count(id) from user where email = :email  limit 1")
    suspend fun checkUser(email: String): Int
}