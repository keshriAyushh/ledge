package com.ayush.datastore.data.repository


import com.ayush.datastore.data.AppDataStore
import com.ayush.datastore.domain.repository.AppDataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: AppDataStore
) : AppDataStoreRepository {

    override suspend fun setSignedIn() {
        dataStore.putValue(
            key = AppDataStore.PreferencesKey.IS_LOGGED_IN,
            value = true
        )
    }

    override suspend fun isSignedIn(): Boolean {
        return dataStore.getValue(AppDataStore.PreferencesKey.IS_LOGGED_IN, false).first()
    }

    override suspend fun clearData() {
        dataStore.clear()
    }
}