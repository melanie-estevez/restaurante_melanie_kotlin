package com.ute.restauranteapp.di

import com.ute.restauranteapp.data.repository.AuthRepositoryImpl
import com.ute.restauranteapp.data.repository.CategoriaMenuRepositoryImpl
import com.ute.restauranteapp.data.repository.ClienteRepositoryImpl
import com.ute.restauranteapp.data.repository.PedidoRepositoryImpl
import com.ute.restauranteapp.data.repository.PlatoRepositoryImpl
import com.ute.restauranteapp.data.repository.UserRepositoryImpl
import com.ute.restauranteapp.domain.repository.AuthRepository
import com.ute.restauranteapp.domain.repository.CategoriaMenuRepository
import com.ute.restauranteapp.domain.repository.ClienteRepository
import com.ute.restauranteapp.domain.repository.PedidoRepository
import com.ute.restauranteapp.domain.repository.PlatoRepository
import com.ute.restauranteapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCategoriaMenuRepository(
        impl: CategoriaMenuRepositoryImpl
    ): CategoriaMenuRepository

    @Binds
    @Singleton
    abstract fun bindClienteRepository(
        impl: ClienteRepositoryImpl
    ): ClienteRepository

    @Binds
    @Singleton
    abstract fun bindPedidoRepository(
        impl: PedidoRepositoryImpl
    ): PedidoRepository

    @Binds
    @Singleton
    abstract fun bindPlatoRepository(
        impl: PlatoRepositoryImpl
    ): PlatoRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}