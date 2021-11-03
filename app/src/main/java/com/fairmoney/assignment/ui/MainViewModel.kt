package com.fairmoney.assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fairmoney.assignment.repo.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainViewModel(
    private val repository: UserRepository
): ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val users = repository.getUsers().cachedIn(viewModelScope)
}