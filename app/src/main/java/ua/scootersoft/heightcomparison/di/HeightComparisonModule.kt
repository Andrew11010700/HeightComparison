package ua.scootersoft.heightcomparison.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import ua.scootersoft.heightcomparison.data.dao.PersonDao
import ua.scootersoft.heightcomparison.repository.HeightComparisonsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HeightComparisonModule {

    @Provides
    @Singleton
    fun provideComparisonRepository(personDao: PersonDao): HeightComparisonsRepository {
        return HeightComparisonsRepository(personDao)
    }

}