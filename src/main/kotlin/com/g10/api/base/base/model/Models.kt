package com.g10.api.base.base.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.g10.api.base.auth.AppUser
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull

@MappedSuperclass
open class AppBaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @JsonIgnore
    @CreationTimestamp
    var created: Timestamp? = null

    @JsonIgnore
    @UpdateTimestamp
    var modified: Timestamp? = null

    @JsonIgnore
    var deleted = false
}

@MappedSuperclass
class AppBaseTitleUserModel : AppBaseUserModel() {
    @NotNull
    var title: String? = null
}

@MappedSuperclass
open class AppBaseUserModel : AppBaseModel() {
    @get:JsonIgnore
    @set:JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    var user: AppUser? = null
}
