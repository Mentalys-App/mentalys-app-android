package com.mentalys.app.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mentalys.app.data.local.entity.HandwritingTestEntity
import com.mentalys.app.data.local.entity.RemoteEntity
import com.mentalys.app.data.local.room.HistoryDatabase
import com.mentalys.app.data.remote.retrofit.MainApiService

@OptIn(ExperimentalPagingApi::class)
class HandwritingTestRemoteMediator(
    private val apiService: MainApiService,
    private val database: HistoryDatabase,
    private val token: String,
    private val startDate: String? = null,
    private val endDate: String? = null,
    private val sortBy: String = "timestamp",
    private val sortOrder: String = "desc"
) : RemoteMediator<Int, HandwritingTestEntity>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HandwritingTestEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                Log.d("RemoteMediator", "REFRESH: Calculating initial page")
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Log.d("RemoteMediator", "PREPEND: Calculating previous page")
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                Log.d("RemoteMediator", "APPEND: Calculating next page")
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        Log.d("RemoteMediator", "Total Pages: ${state.pages.size}")
        Log.d("RemoteMediator", "Total Items Loaded: ${state.pages.flatMap { it.data }.size}")
        Log.d("RemoteMediator", "Calculated Page: $page")
        Log.d("RemoteMediator", "Load Type: $loadType")
        Log.d("RemoteMediator", "Current State Pages: ${state.pages.size}")
        Log.d("RemoteMediator", "Limit: ${state.config.pageSize}")
        Log.d("RemoteMediator", "Load Type: $loadType")

        try {
            val response = apiService.getHandwritingHistory(
                token = "Bearer $token",
                page = page,
                limit = state.config.pageSize,
                startDate = startDate,
                endDate = endDate,
                sortBy = sortBy,
                sortOrder = sortOrder
            )
            Log.d("RemoteMediator", "Response status: ${response.status}")
            Log.d("RemoteMediator", "Total items: ${response.total}")
            Log.d("RemoteMediator", "History size: ${response.handwritingHistory.size}")
            Log.d("RemoteMediator", "API Response - Total: ${response.total}")
            Log.d("RemoteMediator", "API Response - Total Pages: ${response.totalPages}")
            Log.d("RemoteMediator", "API Response - Page: ${response.page}")
            Log.d("RemoteMediator", "API Response - Limit: ${response.limit}")
            Log.d("RemoteMediator", "API Response - History Size: ${response.handwritingHistory.size}")

            val endOfPaginationReached = response.handwritingHistory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteDao().deleteRemoteKeys()
                    database.handwritingTestDao().clearAll()
                }

                val keys = response.handwritingHistory.map { historyItem ->
                    RemoteEntity(
                        id = historyItem.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }

                database.remoteDao().insertAll(keys)

                database.handwritingTestDao().insertAll(
                    response.handwritingHistory.map { historyItem ->
                        HandwritingTestEntity(
                            id = historyItem.id,
                            inputData = historyItem.inputData?.toString() ?: "",
                            metadata = historyItem.metadata?.toString() ?: "",
                            predictionResult = historyItem.prediction.result.result ?: "",
                            confidencePercentage = historyItem.prediction.result.confidencePercentage ?: "0%",
                            timestamp = historyItem.timestamp
                        )
                    }
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            Log.e("RemoteMediator", "Load error", exception)
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, HandwritingTestEntity>): RemoteEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { test ->
            database.remoteDao().getRemoteEntityId(test.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, HandwritingTestEntity>): RemoteEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { test ->
            database.remoteDao().getRemoteEntityId(test.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, HandwritingTestEntity>): RemoteEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteDao().getRemoteEntityId(id)
            }
        }
    }
}