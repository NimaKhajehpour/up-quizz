package com.nima.upquizz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDatastore(private val context: Context) {
    companion object{
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("datastore")

        val themeKey = booleanPreferencesKey("Theme")
        val tokenKey = stringPreferencesKey("Token")
    }

    val getTheme: Flow<Boolean> = context.datastore.data
        .map {
            it[themeKey] ?: true
        }

    suspend fun saveTheme(isDark: Boolean){
        context.datastore.edit {
            it[themeKey] = isDark
        }
    }

    val getToken: Flow<String?> = context.datastore.data
        .map {
            it[tokenKey] ?: ""
        }

    suspend fun saveToken(token: String){
        context.datastore.edit {
            it[tokenKey] = token
        }
    }

    suspend fun deleteToken(){
        context.datastore.edit {
            it[tokenKey] = ""
        }
    }
}