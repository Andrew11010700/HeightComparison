package ua.scootersoft.heightcomparison.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.scootersoft.heightcomparison.data.dao.PersonDao
import ua.scootersoft.heightcomparison.model.ComparedPerson

@Database(
    entities = [ComparedPerson::class],
    version = 4
)
abstract class AppDB: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "database"
    }

    abstract fun personDao(): PersonDao

}