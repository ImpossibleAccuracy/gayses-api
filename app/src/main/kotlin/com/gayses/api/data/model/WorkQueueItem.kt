package com.gayses.api.data.model

import com.gayses.api.data.model.base.BaseModel
import jakarta.persistence.*

@Entity
@Table(name = "workqueue")
class WorkQueueItem(
    id: Long?,

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "WorkId", nullable = false)
    val work: Work,

    @Column(name = "`Order`", nullable = false)
    var order: Int,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectId", nullable = false)
    val project: Project,
) : BaseModel<Long>(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkQueueItem) return false
        if (!super.equals(other)) return false

        if (work != other.work) return false
        if (order != other.order) return false
        if (project != other.project) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + work.hashCode()
        result = 31 * result + order
        result = 31 * result + project.hashCode()
        return result
    }

    override fun toString(): String {
        return "WorkQueueItem(id=$id, work=$work, order=$order, project=$project)"
    }
}
