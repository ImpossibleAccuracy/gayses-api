package com.workflow.api.data.model.base

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant

@MappedSuperclass
abstract class BaseModel<T : Serializable>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, updatable = false)
    private var _id: T?,

    @Column(name = "createdAt", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
) {
    val id: T
        get() = _id!!

    val hasId: Boolean
        get() = _id != null

    fun updateId(id: T) {
        this._id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseModel<*>) return false

        if (_id != other._id) return false

        return true
    }

    override fun hashCode(): Int {
        return _id?.hashCode() ?: 0
    }
}