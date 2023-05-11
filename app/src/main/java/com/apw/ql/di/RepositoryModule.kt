package com.apw.ql.di

import com.apw.ql.data.remote.Api
import com.apw.ql.ui.main.DefaultMainRepository
import com.apw.ql.ui.main.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideMainRepository(api: Api, dispatcher: CoroutineDispatcher): MainRepository = DefaultMainRepository(api, dispatcher)
}