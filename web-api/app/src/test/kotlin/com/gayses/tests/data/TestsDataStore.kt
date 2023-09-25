package com.gayses.tests.data

import com.gayses.api.data.model.*
import com.gayses.api.data.model.ProjectNodeUnit
import java.util.concurrent.atomic.AtomicLong

object TestsDataStore {
    val idGenerator = AtomicLong(100L)

    val accounts: List<Account>
        get() = listOf(
            Account(1, "admin@email.com", TestsUtils.encryptPassword("123")),
            Account(2, "user@email.com", TestsUtils.encryptPassword("456"))
        )

    val projects: List<Project>
        get() = listOf(
            Project(1, "Title#1", accounts[0]),
            Project(2, "Title#2", accounts[1]),
            Project(3, "Title#3", accounts[1]),
        )
}