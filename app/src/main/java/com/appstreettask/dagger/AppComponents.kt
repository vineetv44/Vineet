package com.appstreettask.dagger

import com.appstreettask.dagger.modules.NetworkModule
import com.appstreettask.dagger.modules.ViewModelModule
import com.appstreettask.ui.DataListActivity

import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface AppComponents {
    fun injectRepositoryListActivity(activity: DataListActivity)
}
