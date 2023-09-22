package com.gayses.api.data.repository;

import com.gayses.api.data.model.WorkType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorkTypeRepository : JpaRepository<WorkType, Long> {
    fun findByTitleIgnoreCase(title: String): Optional<WorkType>
}