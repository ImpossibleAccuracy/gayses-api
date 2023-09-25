package com.gayses.tests.data

object TestsUtils {
    fun encryptPassword(pass: String) = "pass_$pass"

    fun decryptPassword(hash: String) = hash.substring(5)

    fun generateToken(email: String) = "jwt_$email"

    fun extractTokenSubject(token: String) = token.substring(4)
}