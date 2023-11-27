package cloud.wig.android.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class to handle storing and retrieving a token using DataStore in Android.
 *
 * @property context The Android context used for DataStore operations.
 */
class StoreToken(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")
        val TOKEN = stringPreferencesKey("token")
    }

    /**
     * Gets the token from the preferences.
     *
     * @return A Flow of String representing the stored token. It can be null if no token is stored.
     */
    val getToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN] ?: ""
        }

    /**
     * Function to save a token in the DataStore.
     *
     * @param token The token to be saved.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }
}
