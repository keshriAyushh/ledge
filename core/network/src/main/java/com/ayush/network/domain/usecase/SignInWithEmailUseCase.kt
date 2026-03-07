package com.ayush.network.domain.usecase

import com.ayush.common.models.User
import com.ayush.common.result.ApiResult
import com.ayush.network.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): ApiResult<User> {
        return authRepository.signInWithEmail(
            email = email,
            password = password
        )
    }
}