package com.gayses.tests.store

import com.gayses.api.data.model.WorkType
import com.gayses.api.data.repository.WorkTypeRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*


object MockWorkTypeRepository : MockStore<WorkType>() {
    override fun create() =
        mockk<WorkTypeRepository> {
            every { findByTitleIgnoreCase(any()) }
                .answers {
                    val title = firstArg<String>()

                    TestsDataStore.workTypes.find {
                        it.title.equals(title, true)
                    }.let {
                        Optional.ofNullable(it)
                    }
                }

            every { save(any()) }
                .answers {
                    val item = firstArg<WorkType>()

                    item.updateId(idGenerator.incrementAndGet())

                    item
                }
        }
}