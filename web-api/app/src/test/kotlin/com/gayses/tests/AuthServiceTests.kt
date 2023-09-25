package com.gayses.tests

import com.gayses.api.data.repository.AccountRepository
import com.gayses.api.exception.InvalidServiceArguments
import com.gayses.api.exception.OperationDeniedException
import com.gayses.api.exception.OperationRejectedException
import com.gayses.api.service.auth.AuthService
import com.gayses.api.service.auth.AuthServiceImpl
import com.gayses.tests.data.TestsDataStore
import com.gayses.tests.data.TestsUtils
import com.gayses.tests.store.MockAccountRepository
import com.security.jwt.service.TokenService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder

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
        every { passwordEncoder.encode(any()) }
            .answers {
                val password = firstArg() as String

                TestsUtils.encryptPassword(password)
            }

        every { passwordEncoder.matches(any(), any()) }
            .answers {
                val rawPassword = firstArg() as String
                val encodedPassword = secondArg() as String

                TestsUtils.decryptPassword(encodedPassword) == rawPassword
            }

        every { tokenService.generateToken(any()) }
            .answers {
                val user = firstArg() as UserDetails

                TestsUtils.generateToken(user.username)
            }
    }

    @Nested
    @DisplayName("Login")
    inner class LoginMethod {
        @Test
        fun whenGivesExistedAccountData_thenReturnAuthResult() {
            // arrange
            val account = TestsDataStore.accounts.first()

            val email = account.email
            val password = TestsUtils.decryptPassword(account.passwordHash)

            val expectedEmail = account.email
            val expectedPasswordHash = account.passwordHash
            val expectedToken = TestsUtils.generateToken(expectedEmail)

            // act
            val result = authService.login(email, password)

            // assert
            val emailCaptor = slot<String>()
            verify { accountRepository.findByEmailIgnoreCase(capture(emailCaptor)) }

            Assertions.assertEquals(expectedEmail, emailCaptor.captured)
            Assertions.assertEquals(expectedEmail, result.account.email)
            Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
            Assertions.assertEquals(expectedToken, result.token)
        }

        @Test
        fun whenGivesExistedAccountDataWithLeadingAndTrailingWhitespaces_thenReturnAuthResult() {
            // arrange
            val account = TestsDataStore.accounts.first()

            val email = "     ${account.email}       "
            val password = "    ${TestsUtils.decryptPassword(account.passwordHash)}   "

            val expectedEmail = account.email
            val expectedPasswordHash = account.passwordHash
            val expectedToken = TestsUtils.generateToken(expectedEmail)

            // act
            val result = authService.login(email, password)

            // assert
            val emailCaptor = slot<String>()
            verify { accountRepository.findByEmailIgnoreCase(capture(emailCaptor)) }

            Assertions.assertEquals(expectedEmail, emailCaptor.captured)
            Assertions.assertEquals(expectedEmail, result.account.email)
            Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
            Assertions.assertEquals(expectedToken, result.token)
        }

        @Test
        fun whenGivesNotExistedAccountData_thenThrowsException() {
            val email = "account999@email.com"
            val password = "password"

            Assertions.assertThrows(OperationRejectedException::class.java) {
                authService.login(email, password)
            }
        }

        @Test
        fun whenGivesExistedAccountDataWithWrongPassword_thenThrowsException() {
            val account = TestsDataStore.accounts.first()

            val email = account.email
            val password = "password213123"

            Assertions.assertThrows(OperationRejectedException::class.java) {
                authService.login(email, password)
            }
        }

        @Test
        fun whenGivesInvalidEmail_thenThrowsException() {
            val account = TestsDataStore.accounts.first()

            val email = "       "
            val password = "    ${TestsUtils.decryptPassword(account.passwordHash)}   "

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                authService.login(email, password)
            }
        }

        @Test
        fun whenGivesInvalidPassword_thenThrowsException() {
            val account = TestsDataStore.accounts.first()

            val email = "     ${account.email}       "
            val password = "     "

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                authService.login(email, password)
            }
        }
    }

    @Nested
    @DisplayName("Registration")
    inner class RegistrationMethod {
        @Test
        fun whenGivesValidData_thenReturnAuthResult() {
            // arrange
            val email = "account999@email.com"
            val password = "password"

            val expectedEmail = "account999@email.com"
            val expectedPasswordHash = TestsUtils.encryptPassword("password")
            val expectedToken = TestsUtils.generateToken(expectedEmail)

            // act
            val result = authService.registration(email, password)

            // assert
            val emailCaptor = slot<String>()
            verify { accountRepository.existsByEmailIgnoreCase(capture(emailCaptor)) }

            Assertions.assertEquals(expectedEmail, emailCaptor.captured)
            Assertions.assertEquals(expectedEmail, result.account.email)
            Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
            Assertions.assertEquals(expectedToken, result.token)
        }

        @Test
        fun whenGivesValidDataWithLeadingAndTrailingWhitespaces_thenReturnAuthResult() {
            // arrange
            val email = "  account999@email.com  "
            val password = "  password  "

            val expectedEmail = "account999@email.com"
            val expectedPasswordHash = TestsUtils.encryptPassword("password")
            val expectedToken = TestsUtils.generateToken(expectedEmail)

            // act
            val result = authService.registration(email, password)

            // assert
            val emailCaptor = slot<String>()
            verify { accountRepository.existsByEmailIgnoreCase(capture(emailCaptor)) }

            Assertions.assertEquals(expectedEmail, emailCaptor.captured)
            Assertions.assertEquals(expectedEmail, result.account.email)
            Assertions.assertEquals(expectedPasswordHash, result.account.passwordHash)
            Assertions.assertEquals(expectedToken, result.token)
        }

        @Test
        fun whenGivesExistedAccountData_thenThrowsException() {
            val account = TestsDataStore.accounts.first()

            val email = account.email
            val password = TestsUtils.decryptPassword(account.password)

            Assertions.assertThrows(OperationDeniedException::class.java) {
                authService.registration(email, password)
            }
        }

        @Test
        fun whenGivesInvalidEmail_thenThrowsException() {
            // arrange
            val email = "   "
            val password = "password"

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                authService.registration(email, password)
            }
        }

        @Test
        fun whenGivesInvalidPassword_thenThrowsException() {
            // arrange
            val email = "  account999@email.com "
            val password = "   "

            Assertions.assertThrows(InvalidServiceArguments::class.java) {
                authService.registration(email, password)
            }
        }
    }
}