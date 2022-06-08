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
        it[EMAIL_KEY] ?: "",
        it[ID_KEY] ?: 0,
        it[TOKEN_KEY] ?: "",
        it[ISLOGIN_KEY] ?: false,
        it[ISFIRSTINSTALL] ?: true
      )
    }
  }

  suspend fun saveUser(user: UserModel) {
    dataStore.edit {
      it[NAME_KEY] = user.name
      it[EMAIL_KEY] = user.email
      it[ID_KEY] = user.userid
      it[TOKEN_KEY] = user.token
      it[ISLOGIN_KEY] = user.islogin
      it[ISFIRSTINSTALL] = user.isfirstinstall
    }
  }

  suspend fun logout() {
    dataStore.edit {
      it[NAME_KEY] = ""
      it[EMAIL_KEY] = ""
      it[ID_KEY] = 0
      it[TOKEN_KEY] = ""
      it[ISLOGIN_KEY] = false
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: UserPreference? = null

    private val NAME_KEY = stringPreferencesKey("name")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val ID_KEY = intPreferencesKey("userid")
    private val ISLOGIN_KEY = booleanPreferencesKey("islogin")
    private val ISFIRSTINSTALL = booleanPreferencesKey("isfirst")


    fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
      return INSTANCE ?: synchronized(this) {
        val instance = UserPreference(dataStore)
        INSTANCE = instance
        instance
      }
    }
  }
}