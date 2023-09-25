package com.workflow.api.data.model

import com.workflow.api.data.model.base.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "performer")
class Performer(
    id: Long?,

    @Column(name = "Title", nullable = false)
    var title: String
) : BaseModel<Long>(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Performer) return false
        if (!super.equals(other)) return false

        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }

    override fun toString(): String {
        return "Performer(id=$id, title='$title')"
    }
}
