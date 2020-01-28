package com.appstreettask.data.repository

import com.appstreettask.data.remote.ApiService
import com.appstreettask.ui.model.DataDetailsItem
import io.reactivex.Single
import javax.inject.Inject

open class DataRepository @Inject
constructor(val apiService: ApiService) {

    fun getDataRepositoryList(): Single<List<DataDetailsItem>> {
        return apiService.getRepositoryList("java", "weekely")
    }
}
