package com.example.melichallenge.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object SearchPreferences {

    private const val SEARCHES_KEY = "last_searches"

    private val Context.dataStore by preferencesDataStore(name = "search_prefs")

    private val searchesKey = stringSetPreferencesKey(SEARCHES_KEY)

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun saveSearch(context: Context, query: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[searchesKey]?.toMutableList() ?: mutableListOf()
            current.remove(query)
            current.add(0, query)
            if (current.size > 10) current.removeLast()
            prefs[searchesKey] = current.toSet()
        }
    }

    fun getLastSearches(context: Context): Flow<List<String>> {
        return context.dataStore.data
            .map { prefs ->
                prefs[searchesKey]?.toList() ?: emptyList()
            }
    }
}

