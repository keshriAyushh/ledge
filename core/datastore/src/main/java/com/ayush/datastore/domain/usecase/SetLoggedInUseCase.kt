package com.ayush.datastore.domain.usecase

import com.ayush.datastore.domain.repository.AppDataStoreRepository
import javax.inject.Inject

class SetLoggedInUseCase @Inject constructor(
    private val appDataStoreRepository: AppDataStoreRepository
) {
    suspend operator fun invoke() {
        appDataStoreRepository.setSignedIn()
    }
}