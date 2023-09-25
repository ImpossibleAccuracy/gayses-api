package com.gayses.tests.store

import com.gayses.tests.data.TestsDataStore
import org.springframework.data.jpa.repository.JpaRepository

abstract class MockStore<T> {
    protected val idGenerator = TestsDataStore.idGenerator

    abstract fun create(): JpaRepository<T, *>
}