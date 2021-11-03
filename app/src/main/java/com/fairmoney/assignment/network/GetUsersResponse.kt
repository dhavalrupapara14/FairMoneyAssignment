package com.fairmoney.assignment.network

import com.fairmoney.assignment.db.User

data class GetUsersResponse(
    val data: List<User>,
    val total: Int,
    val page: Int,
    val limit: Int
)
