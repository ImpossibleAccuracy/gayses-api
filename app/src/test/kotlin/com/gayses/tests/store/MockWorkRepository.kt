package com.gayses.tests.store

import com.gayses.api.data.model.Work
import com.gayses.api.data.repository.WorkRepository
import com.gayses.tests.data.TestsDataStore
import io.mockk.every
import io.mockk.mockk
import java.util.*


object MockWorkRepository : MockStore<Work>() {
    override fun create() =
        mockk<WorkRepository> {
            every { findBy_idAndQueueItem_Project_Id(any(), any()) }
                .answers {
                    val workId = firstArg<Long>()
                    val projectId = secondArg<Long>()

                    TestsDataStore.workQueueItems.find {
                        it.work.id == workId && it.project.id == projectId
                    }.let {
                        Optional.ofNullable(it?.work)
                    }
                }

            every { save(any()) }
                .answers {
                    val item = firstArg<Work>()

                    if (!item.hasId) {
                        item.updateId(idGenerator.incrementAndGet())
                    }

                    item
                }
        }
}