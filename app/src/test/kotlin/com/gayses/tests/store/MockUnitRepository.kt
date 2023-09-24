package com.gayses.tests.store

import com.gayses.api.data.model.Unit
import com.gayses.api.data.repository.UnitRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*


object MockUnitRepository : MockStore<Unit>() {
    override fun create() =
        mockk<UnitRepository> {
            every { findByTitleIgnoreCase(any()) }
                .answers {
                    val title = firstArg<String>()

                    TestsDataStore.units.find {
                        it.title.equals(title, true)
                    }.let {
                        Optional.ofNullable(it)
                    }
                }

            every { save(any()) }
                .answers {
                    val item = firstArg<Unit>()

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }
        }
}