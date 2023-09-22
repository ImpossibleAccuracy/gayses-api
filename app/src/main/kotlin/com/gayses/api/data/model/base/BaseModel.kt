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
}