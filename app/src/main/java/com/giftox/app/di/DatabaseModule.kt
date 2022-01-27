package com.giftox.app.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.giftox.app.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideHomeDao(appDatabase: AppDatabase): HomeDao {
        return appDatabase.homeDao()
    }

    @Provides
    fun provideOccasionDao(appDatabase: AppDatabase): OccasionDao {
        return appDatabase.occasionDao()
    }

    @Provides
    fun provideCelebrityDao(appDatabase: AppDatabase): CelebrityDao {
        return appDatabase.celebrityDao()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideMyOrderDao(appDatabase: AppDatabase): MyOrderDao {
        return appDatabase.myOrderDao()
    }

    @Provides
    fun provideUser(appDatabase: AppDatabase): Flow<User?> {
        return appDatabase.userDao().getLoggedInUser()
    }

}