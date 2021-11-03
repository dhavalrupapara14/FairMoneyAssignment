package com.fairmoney.assignment.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fairmoney.assignment.db.*
import com.fairmoney.assignment.network.ServiceApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val service: ServiceApi,
    private val db: UserDb
) : RemoteMediator<Int, User>() {

    private val userDao: UserDao = db.userDao()
    private val remoteKeyDao: UserRemoteKeyDao = db.userRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        // Get the closest item from PagingState that we want to load data around.
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.getUserRemoteKey()
                    }

                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextKey
                }
            }

            val data = service.getUsers(page = loadKey ?: 1, limit = state.config.pageSize)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.deleteAll()
                    remoteKeyDao.clearRemoteKey()
                }

                remoteKeyDao.insert(UserRemoteKey(id = 1, nextKey = data.page + 1))
                userDao.insertAll(data.data)
            }

            return MediatorResult.Success(endOfPaginationReached = data.data.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

}