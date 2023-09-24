package com.gayses.tests.store

import org.springframework.data.jpa.repository.JpaRepository
import java.util.concurrent.atomic.AtomicLong

abstract class MockStore<T> {
    protected val idGenerator = AtomicLong()

    abstract fun create(): JpaRepository<T, *>
}