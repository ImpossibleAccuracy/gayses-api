package com.gayses.api.data.repository

import com.gayses.api.data.model.Performer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectNodePerformerRepository : JpaRepository<Performer, Long> {
    fun findByTitleIgnoreCase(title: String): Optional<Performer>
}