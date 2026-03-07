package com.ayush.datastore.domain.repository

interface AppDataStoreRepository {
    suspend fun setSignedIn()
    suspend fun isSignedIn(): Boolean
    suspend fun clearData()
}