package com.gayses.tests.store

import com.gayses.api.data.model.Performer
import com.gayses.api.data.repository.PerformerRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*


object MockPerformerRepository : MockStore<Performer>() {
    override fun create() =
        mockk<PerformerRepository> {
            every { findByTitleIgnoreCase(any()) }
                .answers {
                    val title = firstArg<String>()

                    TestsDataStore.performers.find {
                        it.title.equals(title, true)
                    }.let {
                        Optional.ofNullable(it)
                    }
                }

            every { save(any()) }
                .answers {
                    val item = firstArg<Performer>()

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }
        }
}