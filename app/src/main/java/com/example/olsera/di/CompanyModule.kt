package com.example.olsera.di

import android.app.Application
import com.example.olsera.db.CompanyDao
import com.example.olsera.db.CompanyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CompanyModule {

    @Singleton
    @Provides
    fun getCompanyDatabase(context: Application) : CompanyDatabase {
        return CompanyDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun companyDao(companyDatabase: CompanyDatabase) : CompanyDao {
        return companyDatabase.getCompanyDao()
    }

}