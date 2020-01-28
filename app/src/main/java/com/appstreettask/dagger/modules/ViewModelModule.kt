package com.appstreettask.dagger.modules

import androidx.lifecycle.ViewModel
import com.appstreettask.ui.viewmodel.DataViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DataViewModel::class)
    internal abstract fun getDataViewModel(viewModel: DataViewModel): ViewModel
}