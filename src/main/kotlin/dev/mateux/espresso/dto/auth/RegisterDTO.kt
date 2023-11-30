package dev.mateux.espresso.dto.auth

data class RegisterDTO(
    val email: String,
    val password: String,
    val name: String,
)
