package com.ayush.network.domain.usecase

import com.ayush.network.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) {
        authRepository.resetPasswordForEmail(email)
    }
}