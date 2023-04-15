package ua.scootersoft.heightcomparison.screens.heightcomparisons.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import ua.scootersoft.heightcomparison.screens.heightcomparisons.repository.HeightComparisonsRepository

@Module
@InstallIn(SingletonComponent::class)
class HeightComparisonModule {

    @Provides
    fun provideComparisonRepository(): HeightComparisonsRepository {
        return HeightComparisonsRepository()
    }

}