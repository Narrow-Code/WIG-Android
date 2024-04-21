package wig.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

    /**
     * Class to handle storing and retrieving settings using DataStore in Android.
     *
     * @property context The Android context used for DataStore operations.
     */
    class StoreSettings(private val context: Context) {
        companion object {
            private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
            val IS_VIBRATE_ENABLED = booleanPreferencesKey("is_vibrate_enabled")
            val IS_SOUND_ENABLED = booleanPreferencesKey("is_sound_enabled")
            val IS_STARTUP_ON_SCANNER = booleanPreferencesKey("is_startup_on_scanner")
            val IS_HOSTED = booleanPreferencesKey("is_hosted")
            val HOSTNAME = stringPreferencesKey("hostname")
            val PORT = stringPreferencesKey("port")


        }

        /**
         * Gets the isVibrate flag from the preferences.
         *
         * @return A Flow of Boolean representing the stored isVibrate flag. It returns false if not found.
         */
        val getIsVibrateEnabled: Flow<Boolean> = context.dataStore.data
            .map { preferences ->
                preferences[IS_VIBRATE_ENABLED] ?: false
            }

        /**
         * Gets the isVibrate flag from the preferences.
         *
         * @return A Flow of Boolean representing the stored isVibrate flag. It returns false if not found.
         */
        val getIsStartupOnScanner: Flow<Boolean> = context.dataStore.data
            .map { preferences ->
                preferences[IS_STARTUP_ON_SCANNER] ?: false
            }

        /**
         * Gets the isSoundEnabled flag from the preferences.
         *
         * @return A Flow of Boolean representing the stored isSoundEnabled flag. It returns false if not found.
         */
        val getIsSoundEnabled: Flow<Boolean> = context.dataStore.data
            .map { preferences ->
                preferences[IS_SOUND_ENABLED] ?: false
            }

        /**
         * Function to save the isVibrate flag in the DataStore.
         *
         * @param isVibrateEnabled The isVibrateEnabled flag to be saved.
         */
        suspend fun saveIsVibrateEnabled(isVibrateEnabled: Boolean) {
            context.dataStore.edit { preferences ->
                preferences[IS_VIBRATE_ENABLED] = isVibrateEnabled
            }
        }

        /**
         * Function to save the isSoundEnabled flag in the DataStore.
         *
         * @param isSoundEnabled The isSoundEnabled flag to be saved.
         */
        suspend fun saveIsSoundEnabled(isSoundEnabled: Boolean) {
            context.dataStore.edit { preferences ->
                preferences[IS_SOUND_ENABLED] = isSoundEnabled
            }
        }

        /**
         * Function to save the isStartupOnScanner flag in the DataStore.
         *
         * @param isStartupOnScanner The isStarupOnScanner flag to be saved.
         */
        suspend fun saveIsStartupOnScanner(isStartupOnScanner: Boolean) {
            context.dataStore.edit { preferences ->
                preferences[IS_STARTUP_ON_SCANNER] = isStartupOnScanner
            }
        }

        val getHostname: Flow<String> = context.dataStore.data
            .map { preferences ->
                preferences[HOSTNAME] ?: ""
            }

        suspend fun saveHostname(hostname: String) {
            context.dataStore.edit { preferences ->
                preferences[HOSTNAME] = hostname
            }
        }

        val getPort: Flow<String> = context.dataStore.data
            .map { preferences ->
                preferences[PORT] ?: ""
            }

        suspend fun savePort(port: String) {
            context.dataStore.edit { preferences ->
                preferences[PORT] = port
            }
        }

        suspend fun saveIsHosted(isHosted: Boolean) {
            context.dataStore.edit { preferences ->
                preferences[IS_HOSTED] = isHosted
            }
        }

        val getIsHosted: Flow<Boolean> = context.dataStore.data
            .map { preferences ->
                preferences[IS_HOSTED] ?: false
            }

    }