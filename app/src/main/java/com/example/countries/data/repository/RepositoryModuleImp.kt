package com.example.countries.data.repository

import com.example.countries.data.network.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleImp {
  @Provides
  fun  repositoryImplementation(service : ApiInterface) : Repository{
    return Repository(service)
  }
}