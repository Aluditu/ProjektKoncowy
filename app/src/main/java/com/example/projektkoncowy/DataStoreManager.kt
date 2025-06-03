package com.example.projektkoncowy.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {


    private object Keys {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val IMAGE_PATH = stringPreferencesKey("image_path")
    }

    val firstNameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[Keys.FIRST_NAME] ?: "" }

    val lastNameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[Keys.LAST_NAME] ?: "" }

    val imagePathFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[Keys.IMAGE_PATH] ?: "" }


    suspend fun saveFirstName(firstName: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.FIRST_NAME] = firstName
        }
    }

    suspend fun saveLastName(lastName: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.LAST_NAME] = lastName
        }
    }

    suspend fun saveImagePath(path: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IMAGE_PATH] = path
        }
    }
}
