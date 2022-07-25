package com.example.marvelapp.framework.di

import com.igaopk10.core.usecase.GetCharacterCategories
import com.igaopk10.core.usecase.GetCharacterCategoriesImpl
import com.igaopk10.core.usecase.GetCharactersUseCase
import com.igaopk10.core.usecase.GetCharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindCharactersUseCase(useCase: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    fun bindGetComicsUseCase(useCase: GetCharacterCategoriesImpl): GetCharacterCategories
}