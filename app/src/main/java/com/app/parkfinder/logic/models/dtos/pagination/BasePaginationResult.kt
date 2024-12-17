package com.app.parkfinder.logic.models.dtos.pagination

data class BasePaginationResult<T> (
    val totalItems: Int,
    val totalPages: Int,
    val page: Int,
    val limit: Int,
    val items: List<T>
)