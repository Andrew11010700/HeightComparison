package ua.scootersoft.heightcomparison.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.scootersoft.heightcomparison.model.ComparedPerson
import ua.scootersoft.heightcomparison.utils.Constants

@Dao
interface PersonDao {

    @Query("SELECT * FROM ${Constants.PEOPLE_TABLE}")
    fun getAllPersonsUpdatedData(): Flow<List<ComparedPerson>>

    @Query("SELECT * FROM ${Constants.PEOPLE_TABLE} WHERE isShowPerson = :isShowPerson ORDER BY sortIndex")
    fun getVisibleItems(isShowPerson: Boolean = true): List<ComparedPerson>

    @Query("SELECT * FROM ${Constants.PEOPLE_TABLE} WHERE id = :id")
    fun getPersonById(id: Long): ComparedPerson

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(people: List<ComparedPerson>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: ComparedPerson): Long

    @Query("SELECT count(*) FROM ${Constants.PEOPLE_TABLE}")
    fun recordsCount(): Int

    @Update
    fun update(person: ComparedPerson)

    @Update
    fun updateAll(people: List<ComparedPerson>)

    @Delete
    fun delete(person: ComparedPerson)

}