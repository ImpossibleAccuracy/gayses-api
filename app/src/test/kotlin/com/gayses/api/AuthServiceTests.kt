package com.gayses.api

import com.gayses.api.data.repository.AccountRepository
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.OperationRejectedException
import com.gayses.api.service.auth.AuthService
import com.gayses.api.service.auth.AuthServiceImpl
import com.gayses.api.store.MockAccountRepository
import com.security.jwt.service.TokenService
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockKExtension::class)
class AuthServiceTests {
    private val accountRepository: AccountRepository = MockAccountRepository.create()

    private val tokenService: TokenService = mockk()

    private val passwordEncoder: PasswordEncoder = mockk()

    private val authService: AuthService = AuthServiceImpl(
        tokenService,
        accountRepository,
        passwordEncoder
    )

    @BeforeEach
    fun setup() {
        coEvery { passwordEncoder.encode(any()) }
            .answers {
                val password = firstArg() as String

                MockAccountRepository.encryptPassword(password)
            }

        coEvery { passwordEncoder.matches(any(), any()) }
            .answers {
                val rawPassword = firstArg() as String
                val encodedPassword = secondArg() as String

                MockAccountRepository.decryptPassword(encodedPassword) == rawPassword
            }

        coEvery { tokenService.generateToken(any()) }
            .answers {
                val user = firstArg() as UserDetails

                "${user.username}_${user.password}"
            }
    }

    @Test
    fun login_whenGivesExistedAccountData_thenReturnAuthResult() {
        // arrange
        val account = MockAccountRepository.data.first()

        val email = account.email
        val password = MockAccountRepository.decryptPassword(account.passwordHash)

        val expectedEmail = account.email
        val expectedPasswordHash = account.passwordHash
        val expectedToken = "${expectedEmail}_${expectedPasswordHash}"

        // act
        val result = authService.login(email, password)

        // assert
        Assertions.assertEquals(expectedEmail, result.account.email)
        Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
        Assertions.assertEquals(expectedToken, result.token)
    }

    @Test
    fun login_whenGivesExistedAccountDataWithLeadingAndTrailingWhitespaces_thenReturnAuthResult() {
        // arrange
        val account = MockAccountRepository.data.first()

        val email = "     ${account.email}       "
        val password = "    ${MockAccountRepository.decryptPassword(account.passwordHash)}   "

        val expectedEmail = account.email
        val expectedPasswordHash = account.passwordHash
        val expectedToken = "${expectedEmail}_${expectedPasswordHash}"

        // act
        val result = authService.login(email, password)

        // assert
        Assertions.assertEquals(expectedEmail, result.account.email)
        Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
        Assertions.assertEquals(expectedToken, result.token)
    }

    @Test
    fun login_whenGivesNotExistedAccountData_thenThrowsException() {
        val email = "account999@email.com"
        val password = "password"

        Assertions.assertThrows(OperationRejectedException::class.java) {
            authService.login(email, password)
        }
    }

    @Test
    fun login_whenGivesExistedAccountDataWithWrongPassword_thenThrowsException() {
        val account = MockAccountRepository.data.first()

        val email = account.email
        val password = "password213123"

        Assertions.assertThrows(OperationRejectedException::class.java) {
            authService.login(email, password)
        }
    }

    @Test
    fun login_whenGivesInvalidEmail_thenThrowsException() {
        val account = MockAccountRepository.data.first()

        val email = "       "
        val password = "    ${MockAccountRepository.decryptPassword(account.passwordHash)}   "

        Assertions.assertThrows(InvalidServiceArguments::class.java) {
            authService.login(email, password)
        }
    }

    @Test
    fun login_whenGivesInvalidPassword_thenThrowsException() {
        val account = MockAccountRepository.data.first()

        val email = "     ${account.email}       "
        val password = "     "

        Assertions.assertThrows(InvalidServiceArguments::class.java) {
            authService.login(email, password)
        }
    }

    @Test
    fun registration_whenGivesValidData_thenReturnAuthResult() {
        // arrange
        val email = "account999@email.com"
        val password = "password"

        val expectedEmail = "account999@email.com"
        val expectedPasswordHash = MockAccountRepository.encryptPassword("password")
        val expectedToken = "${expectedEmail}_${expectedPasswordHash}"

        // act
        val result = authService.registration(email, password)

        // assert
        Assertions.assertEquals(expectedEmail, result.account.email)
        Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
        Assertions.assertEquals(expectedToken, result.token)
    }

    @Test
    fun registration_whenGivesValidDataWithLeadingAndTrailingWhitespaces_thenReturnAuthResult() {
        // arrange
        val email = "  account999@email.com  "
        val password = "  password  "

        val expectedEmail = "account999@email.com"
        val expectedPasswordHash = MockAccountRepository.encryptPassword("password")
        val expectedToken = "${expectedEmail}_${expectedPasswordHash}"

        // act
        val result = authService.registration(email, password)

        // assert
        Assertions.assertEquals(expectedEmail, result.account.email)
        Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
        Assertions.assertEquals(expectedToken, result.token)
    }

    @Test
    fun registration_whenGivesExistedAccountData_thenThrowsException() {
        val account = MockAccountRepository.data.first()

        val email = account.email
        val password = "password"

        Assertions.assertThrows(OperationRejectedException::class.java) {
            authService.registration(email, password)
        }
    }

    @Test
    fun registration_whenGivesInvalidEmail_thenThrowsException() {
        // arrange
        val email = "   "
        val password = "password"

        Assertions.assertThrows(InvalidServiceArguments::class.java) {
            authService.registration(email, password)
        }
    }

    @Test
    fun registration_whenGivesInvalidPassword_thenThrowsException() {
        // arrange
        val email = "  account999@email.com "
        val password = "   "

        Assertions.assertThrows(InvalidServiceArguments::class.java) {
            authService.registration(email, password)
        }
    }
}