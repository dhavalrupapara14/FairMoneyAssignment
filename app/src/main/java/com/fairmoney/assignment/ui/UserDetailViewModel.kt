package com.fairmoney.assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fairmoney.assignment.db.User
import com.fairmoney.assignment.repo.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class UserDetailViewModel(
    private val repository: UserRepository
): ViewModel() {
    suspend fun getUser(id: String) = repository.getUser(id)
}