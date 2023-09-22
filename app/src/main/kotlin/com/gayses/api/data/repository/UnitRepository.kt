package com.gayses.api.data.repository;

import com.gayses.api.data.model.Unit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UnitRepository : JpaRepository<Unit, Long> {
    fun findByTitleIgnoreCase(title: String): Optional<Unit>
}