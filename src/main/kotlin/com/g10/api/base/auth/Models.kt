package com.g10.api.base.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.g10.api.base.base.model.AppBaseModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["username", "email"])])
class AppUser : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @NotEmpty
    @NotBlank
    @NotNull
    var firstName: String = ""

    @NotEmpty
    @NotBlank
    @NotNull
    var lastName: String = ""

    @Email(message = "The email provided is not a valid email.")
    @NotEmpty
    @NotBlank
    @NotNull
    var email: String = ""

    @NotEmpty
    @NotBlank
    @NotNull
    @Column(unique = true)
    private var username: String = ""

    @field:NotEmpty
    @field:NotBlank
    @field:NotNull
    private var password: String = ""

    private val enabled = true
    private val accountNonExpired = true
    private val accountNonLocked = true
    private val credentialsNonExpired = true

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    private val roles: Set<Role?> = HashSet()

    @OneToOne(mappedBy = "user")
    private val profile: Profile? = null

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return roles
    }

    @JsonIgnore
    override fun getPassword(): String {
        return password
    }

    @JsonProperty
    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return accountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return accountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}

@Entity
class Role : GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id = 0
    private var authority: String? = null

    @ManyToMany(mappedBy = "roles")
    var users: Set<AppUser> = HashSet()

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var permissions: Set<Permission> = HashSet()

    override fun getAuthority(): String {
        return authority!!
    }

    fun setAuthority(authority: String?) {
        this.authority = authority
    }
}

@Entity
class Permission : GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id = 0
    private var authority: String? = null

    @ManyToMany(mappedBy = "permissions")
    var roles: Set<Role> = java.util.HashSet()

    override fun getAuthority(): String {
        return authority!!
    }

    fun setAuthority(authority: String?) {
        this.authority = authority
    }
}

@Entity
class Avatar(private val url: String) : AppBaseModel() {
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private val user: AppUser? = null
}

@Entity
class Profile : AppBaseModel() {
    private val budget = 0
    private val income = 0

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private val user: AppUser? = null
}