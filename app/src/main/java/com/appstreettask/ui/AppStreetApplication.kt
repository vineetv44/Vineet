package com.appstreettask.ui

import android.app.Application
import android.content.Context
import com.appstreettask.image_cache.ImageLoader


class AppStreetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {

        fun getAppContext():Context {
            return context
        }

        fun getImageLoader() : ImageLoader {
            return ImageLoader(getAppContext())
        }

        lateinit var context: Context
    }
}
