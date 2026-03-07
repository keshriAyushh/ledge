package com.ayush.network.domain.usecase

import com.ayush.common.result.ApiResult
import com.ayush.network.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): ApiResult<Unit> {
        return authRepository.resetPasswordForEmail(email)
    }
}