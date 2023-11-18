package ua.scootersoft.heightcomparison.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.scootersoft.heightcomparison.data.AppDB
import ua.scootersoft.heightcomparison.data.dao.PersonDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Provides
    @Singleton
    fun providePersonDao(database: AppDB): PersonDao {
        return database.personDao()
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) : AppDB {
        return Room
            .databaseBuilder(context, AppDB::class.java, AppDB.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}