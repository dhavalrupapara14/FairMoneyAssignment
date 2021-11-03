package com.fairmoney.assignment.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import com.fairmoney.assignment.db.UserDb
import com.fairmoney.assignment.db.UserDetail
import com.fairmoney.assignment.network.ServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserRepository(private val db: UserDb, private val serviceApi: ServiceApi) {
    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUsers() =  Pager(
        config = PagingConfig(NETWORK_PAGE_SIZE),
        remoteMediator = UserRemoteMediator(serviceApi, db)
    ) {
        db.userDao().getUsers()
    }.flow

    suspend fun getUser(id: String): Flow<UserDetail?> {
        try {
            val data = serviceApi.getUser(id)

            db.withTransaction {
                db.userDetailDao().insert(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return flowOf(
            try {
                db.userDetailDao().getUser(id)
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }
        )
    }
}