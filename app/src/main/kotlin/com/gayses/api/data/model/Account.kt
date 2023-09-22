package com.gayses.api.data.model

import com.gayses.api.data.model.base.BaseModel
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "account")
class Account(
    id: Long?,

    @Column(name = "Email", nullable = false)
    var email: String,

    @Column(name = "Password", nullable = false)
    var passwordHash: String,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    val projects: Set<Project> = setOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "Account_Role",
        joinColumns = [JoinColumn(name = "AccountId")],
        inverseJoinColumns = [JoinColumn(name = "RoleId")]
    )
    val roles: Set<Role> = setOf()
) : BaseModel<Long>(id), UserDetails {
    override fun getAuthorities(): Set<GrantedAuthority> =
        roles
            .map {
                SimpleGrantedAuthority("ROLE_${it.title}")
            }
            .toSet()

    override fun getPassword(): String =
        passwordHash

    override fun getUsername(): String =
        email

    override fun isAccountNonExpired(): Boolean =
        true

    override fun isAccountNonLocked(): Boolean =
        true

    override fun isCredentialsNonExpired(): Boolean =
        true

    override fun isEnabled(): Boolean =
        true
}
