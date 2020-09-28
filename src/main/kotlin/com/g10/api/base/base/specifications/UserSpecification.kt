package com.g10.api.base.base.specifications

import com.g10.api.base.base.model.AppBaseUserModel
import net.kaczmarzyk.spring.data.jpa.domain.Equal
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec
import org.springframework.data.jpa.domain.Specification

@Spec(path = "user.id", constVal = ["#{settings.getAuthenticatedUser().id}"], valueInSpEL = true, spec = Equal::class)
interface UserSpecification<S : AppBaseUserModel?> : Specification<S>