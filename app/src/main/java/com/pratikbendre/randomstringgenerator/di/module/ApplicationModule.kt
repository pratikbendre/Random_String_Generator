package com.pratikbendre.randomstringgenerator.di.module

import android.content.ContentResolver
import android.content.Context
import com.google.gson.Gson
import com.pratikbendre.randomstringgenerator.utils.DefaultDispatcherProvider
import com.pratikbendre.randomstringgenerator.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()


    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}