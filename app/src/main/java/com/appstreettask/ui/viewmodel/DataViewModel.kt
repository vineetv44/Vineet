package com.appstreettask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appstreettask.data.repository.DataRepository
import com.appstreettask.ui.model.DataDetailsItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class DataViewModel @Inject
constructor(internal var dataRepository: DataRepository) : ViewModel() {
    var dataList = MutableLiveData<List<DataDetailsItem>>()
    var onError = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var disposable = CompositeDisposable()

    fun getRepositoryListData() {
        loading.value = true
        disposable.add(
            dataRepository.getDataRepositoryList().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeWith(object : DisposableSingleObserver<List<DataDetailsItem>>() {
                override fun onSuccess(value: List<DataDetailsItem>) {
                    dataList.value = value
                    onError.value = false
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    onError.value = true
                    loading.value = false
                }
            })
        )
    }

    val githubRepositoryList: LiveData<List<DataDetailsItem>>
        get() = dataList

    val githubRepositoryError: LiveData<Boolean>
        get() = onError

    val loadingState: LiveData<Boolean>
        get() = loading


}
