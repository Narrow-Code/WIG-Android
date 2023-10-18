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
 * Class to handle storing and retrieving a users UID using DataStore in Android.
 *
 * @property context The Android context used for DataStore operations.
 */
class StoreUserUID(private val context: Context) {
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_uid")
        val USER_UID = stringPreferencesKey("user_uid")
    }

    /**
     * Gets the users UID from the preferences.
     *
     * @return A Flow of String representing the stored UID. It can be null if no token is stored.
     */
    val getUID: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_UID] ?: ""
        }

    /**
     * Function to save a UID in the DataStore.
     *
     * @param uid The UID to be saved.
     */
    suspend fun saveUID(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_UID] = uid
        }
    }

}