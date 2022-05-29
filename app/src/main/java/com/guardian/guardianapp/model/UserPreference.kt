package com.guardian.guardianapp.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {

  fun getUser(): Flow<UserModel> {
    return dataStore.data.map {
      UserModel(
        it[NAME_KEY] ?: "",
        it[TOKEN_KEY] ?: "",
      )
    }
  }

  suspend fun saveUser(user: UserModel) {
    dataStore.edit {
      it[NAME_KEY] = user.name
      it[TOKEN_KEY] = user.token
    }
  }

  suspend fun logout() {
    dataStore.edit {
      it[NAME_KEY] = ""
      it[TOKEN_KEY] = ""
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: UserPreference? = null

    private val NAME_KEY = stringPreferencesKey("name")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
      return INSTANCE ?: synchronized(this) {
        val instance = UserPreference(dataStore)
        INSTANCE = instance
        instance
      }
    }
  }
}