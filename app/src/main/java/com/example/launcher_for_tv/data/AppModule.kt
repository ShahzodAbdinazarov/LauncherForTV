package com.example.launcher_for_tv.data

import android.app.Application
import android.content.Context
import com.example.launcher_for_tv.utils.DefaultLauncherHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideDefaultLauncherHelper(context: Context): DefaultLauncherHelper {
        return DefaultLauncherHelper(context)
    }
}
