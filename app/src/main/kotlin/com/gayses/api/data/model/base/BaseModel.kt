package com.gayses.api.data.model.base

import jakarta.persistence.*
import java.io.Serializable

@MappedSuperclass
abstract class BaseModel<T : Serializable>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, updatable = false)
    private val _id: T?
) {
    val id: T
        get() = _id!!

    val hasId: Boolean
        get() = _id != null

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