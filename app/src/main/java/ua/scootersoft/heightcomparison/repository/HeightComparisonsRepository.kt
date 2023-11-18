package ua.scootersoft.heightcomparison.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ua.scootersoft.heightcomparison.data.dao.PersonDao
import ua.scootersoft.heightcomparison.model.ComparedPerson

class HeightComparisonsRepository(private val personDao: PersonDao) {

    fun isPersonsEmpty(): Boolean = personDao.recordsCount() == 0

    fun getComparisonPeople(): Flow<List<ComparedPerson>> {
        return personDao.getAllPersonsUpdatedData()
    }

    fun insertAll(people: List<ComparedPerson>):List<Long> {
        return personDao.insertAll(people)
    }

    fun updatePersonName(person: ComparedPerson, newName: String) {
        personDao.update(person.copy(name = newName))
    }

    fun updatePersonHeight(person: ComparedPerson, heightCm: Int) {
        personDao.update(person.copy(heightCm = heightCm))
    }

    fun updatePersonImage(person: ComparedPerson, imageUrl: String) {
        personDao.update(person.copy(imageUrl = imageUrl))
    }

    fun addPerson(person: ComparedPerson) {
        val id = personDao.insert(person)
        val comparedPerson = personDao.getPersonById(id)
        comparedPerson.sortIndex = id
        personDao.update(comparedPerson)
    }

    fun removePerson(person: ComparedPerson) {
        personDao.delete(person)
    }

    fun removeImage(person: ComparedPerson) {
        personDao.update(person.copy(imageUrl = null))
    }

    fun updatePerson(person: ComparedPerson) {
        personDao.update(person)
    }

    fun updateAll(people: List<ComparedPerson>) {
        personDao.updateAll(people)
    }

    fun swapPositions(currentIndex: Int, nextIndex: Int) {
        val allPeople = personDao.getVisibleItems()
        val currentPerson = allPeople[currentIndex]
        val nextPerson = allPeople[nextIndex]
        val tmpIndex = currentPerson.sortIndex
        currentPerson.sortIndex = nextPerson.sortIndex
        nextPerson.sortIndex = tmpIndex
        Log.d("ComparedPerson", "swapPositions: current person = $currentPerson, next person = $nextPerson")
        personDao.update(currentPerson)
        personDao.update(nextPerson)
    }

    fun getVisibleItems(): List<ComparedPerson> {
        return personDao.getVisibleItems()
    }

}