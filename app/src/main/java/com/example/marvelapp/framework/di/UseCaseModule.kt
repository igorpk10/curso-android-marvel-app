package com.example.marvelapp.framework.di

import com.igaopk10.core.usecase.GetCharacterCategoriesImpl
import com.igaopk10.core.usecase.GetCharacterCategories
import com.igaopk10.core.usecase.GetCharactersUseCaseImpl
import com.igaopk10.core.usecase.GetCharactersUseCase
import com.igaopk10.core.usecase.RemoveFavoriteUseCase
import com.igaopk10.core.usecase.RemoveFavoriteUseCaseImpl
import com.igaopk10.core.usecase.CheckFavoriteUseCaseImpl
import com.igaopk10.core.usecase.CheckFavoriteUseCase
import com.igaopk10.core.usecase.AddFavoriteUseCaseImpl
import com.igaopk10.core.usecase.AddFavoriteUseCase
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

    @Binds
    fun bindCheckFavoriteUseCase(useCase: CheckFavoriteUseCaseImpl): CheckFavoriteUseCase

    @Binds
    fun bindAddFavoritesUseCase(useCase: AddFavoriteUseCaseImpl): AddFavoriteUseCase

    @Binds
    fun bindRemoveFavoritesUseCase(useCase: RemoveFavoriteUseCaseImpl): RemoveFavoriteUseCase
}