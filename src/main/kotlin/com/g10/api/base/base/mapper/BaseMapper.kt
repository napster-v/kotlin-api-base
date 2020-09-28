package com.g10.api.base.base.mapper

interface BaseMapper<S, T> {
    /**
     * Converts given model to dto.
     */
    fun toDto(source: S): T

    /**
     * Converts given dto to model.
     */
    fun toModel(target: T): S //
    //
    //    /**
    //     * Converts given model list to dto list.
    //     */
    //    List<T> toDto(List<S> sourceList);
    //
    //    /**
    //     * Converts given dto list to model list.
    //     */
    //    List<S> toModel(List<T> targetList);
}