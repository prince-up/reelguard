package com.example.vigil.di

import android.content.Context
import androidx.room.Room
import com.example.vigil.data.database.AppDatabase
import com.example.vigil.data.database.ReelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reel_counter_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideReelDao(database: AppDatabase): ReelDao {
        return database.reelDao()
    }
}
