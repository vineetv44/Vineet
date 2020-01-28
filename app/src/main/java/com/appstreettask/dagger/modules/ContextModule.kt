package com.appstreettask.dagger.modules

import android.content.Context

import dagger.Module
import dagger.Provides

@Module
class ContextModule(
    @get:Provides
    val context: Context
)
