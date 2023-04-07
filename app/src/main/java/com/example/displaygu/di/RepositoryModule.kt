package com.example.displaygu.di

import com.example.displaygu.network.Api
import com.example.displaygu.ui.DefaultMainRepository
import com.example.displaygu.ui.MainRepository
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