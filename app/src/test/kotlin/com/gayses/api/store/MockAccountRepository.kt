package com.gayses.api.store

import com.gayses.api.data.model.Account
import com.gayses.api.data.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.mockk
import java.util.*

object MockAccountRepository : MockStore<Account>() {
    fun encryptPassword(pass: String) = "pass_$pass"

    fun decryptPassword(hash: String) = hash.substring(4)

    override val data: List<Account>
        get() = listOf(
            Account(1, "admin@email.com", encryptPassword("123")),
            Account(2, "user@email.com", encryptPassword("456"))
        )

    override fun create() =
        mockk<AccountRepository>().also { repo ->
            // FIND BY ID
            coEvery { repo.findById(any()) }
                .returns(Optional.empty())

            data.forEach {
                coEvery { repo.findById(it.id) }
                    .returns(Optional.of(it))
            }

            // FIND EMAIL
            coEvery { repo.findByEmail(any()) }
                .returns(Optional.empty())

            data.forEach {
                coEvery { repo.findByEmail(it.email) }
                    .returns(Optional.of(it))
            }

            coEvery { repo.findByEmailIgnoreCase(any()) }
                .answers { _ ->
                    val email = firstArg() as String

                    val account = data.find {
                        it.email.equals(email, true)
                    }

                    if (account == null) Optional.empty()
                    else Optional.of(account)
                }

            // EXISTS EMAIL
            coEvery { repo.existsByEmailIgnoreCase(any()) }
                .answers { _ ->
                    val email = firstArg() as String

                    data.any {
                        it.email.equals(email, true)
                    }
                }

            // SAVE
            coEvery { repo.save(any()) }
                .answers {
                    val item = firstArg() as Account

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }

            // DELETE
            coEvery { repo.delete(any()) }
                .returns(Unit)
        }
}