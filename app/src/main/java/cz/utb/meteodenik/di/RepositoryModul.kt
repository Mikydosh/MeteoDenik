package cz.utb.meteodenik.di

import cz.utb.meteodenik.data.MeteoRepository
import cz.utb.meteodenik.data.MeteoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModul {
    @Binds
    @Singleton
    abstract fun vazMeteoRepository(impl: MeteoRepositoryImpl): MeteoRepository
}