package com.gayses.api.data.model

import com.gayses.api.data.model.base.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "unit")
class Unit(
    id: Long?,

    @Column(name = "Title", nullable = false, length = 50)
    var title: String
) : BaseModel<Long>(id)
