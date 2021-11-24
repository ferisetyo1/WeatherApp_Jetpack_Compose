package weather.codeid.feri.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "WeatherApp")
class DataStoreManager @Inject constructor(@ApplicationContext private val appContext:Context) {
    private val EMAIL_KEY = stringPreferencesKey("EMAILPREFF")
    val email: Flow<String> = appContext.dataStore.data
        .map { preferences ->
            preferences[EMAIL_KEY] ?: ""
        }

    suspend fun editEmail(string: String) {
        appContext.dataStore.edit {
            it[EMAIL_KEY] = string
        }
    }
}