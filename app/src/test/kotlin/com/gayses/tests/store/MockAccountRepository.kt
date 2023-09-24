package com.gayses.tests.store

import com.gayses.api.data.model.Account
import com.gayses.api.data.repository.AccountRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*

object MockAccountRepository : MockStore<Account>() {
    override fun create() =
        mockk<AccountRepository> {
            // FIND BY ID
            every { findById(any()) }
                .returns(Optional.empty())

            TestsDataStore.accounts.forEach {
                every { findById(it.id) }
                    .returns(Optional.of(it))
            }

            // FIND EMAIL
            every { findByEmail(any()) }
                .returns(Optional.empty())

            TestsDataStore.accounts.forEach {
                every { findByEmail(it.email) }
                    .returns(Optional.of(it))
            }

            every { findByEmailIgnoreCase(any()) }
                .answers { _ ->
                    val email = firstArg() as String

                    val account = TestsDataStore.accounts.find {
                        it.email.equals(email, true)
                    }

                    if (account == null) Optional.empty()
                    else Optional.of(account)
                }

            // EXISTS EMAIL
            every { existsByEmailIgnoreCase(any()) }
                .answers { _ ->
                    val email = firstArg() as String

                    TestsDataStore.accounts.any {
                        it.email.equals(email, true)
                    }
                }

            // SAVE
            every { save(any()) }
                .answers {
                    val item = firstArg() as Account

                    if (!item.hasId) {
                        item.updateId(idGenerator.incrementAndGet())
                    }

                    item
                }

            // DELETE
            every { delete(any()) }
                .returns(Unit)
        }
}