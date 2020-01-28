package com.appstreettask.data.remote


import com.appstreettask.ui.model.DataDetailsItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/developers")
    fun getRepositoryList(@Query("language") language:String,
                          @Query("since") since:String): Single<List<DataDetailsItem>>
}
