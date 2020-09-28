package com.g10.api.base.base.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.g10.api.base.auth.AppUser

open class BaseDTO {
    var id = 0
}

open class BaseTitleDTO : BaseDTO() {
    private val title: String? = null
}

open class BaseUserDTO : BaseDTO() {
    @get:JsonIgnore
    @set:JsonProperty
    var user: AppUser? = null
}
