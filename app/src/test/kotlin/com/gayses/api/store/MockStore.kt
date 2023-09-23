package com.gayses.api.store

import org.springframework.data.jpa.repository.JpaRepository
import java.util.concurrent.atomic.AtomicLong

abstract class MockStore<T> {
    protected val idGenerator = AtomicLong()

    abstract val data: List<T>

    abstract fun create(): JpaRepository<T, *>
}