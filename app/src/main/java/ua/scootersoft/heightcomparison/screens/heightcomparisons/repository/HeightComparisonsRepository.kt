package ua.scootersoft.heightcomparison.screens.heightcomparisons.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.ComparedPerson
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.Gender

class HeightComparisonsRepository {

    private val _comparedPersonFlow = MutableStateFlow<List<ComparedPerson>>(emptyList())

    init {
        if (comparedPerson.isEmpty()) {
            comparedPerson.addAll(listOf(
                ComparedPerson(1, null, Gender.WOMAN, "Average girl",170),
                ComparedPerson(2, null, Gender.MAN,  "Andrew",160),
                ComparedPerson(3, null, Gender.WOMAN,"Tall girl", 179)
            ))
            _comparedPersonFlow.value = comparedPerson
        }
    }

    companion object {
        private var comparedPerson = mutableListOf<ComparedPerson>()
    }

    suspend fun getComparisonPeople(): StateFlow<List<ComparedPerson>> {
        return _comparedPersonFlow.asStateFlow()
    }

    suspend fun updatePersonName(person: ComparedPerson, newName: String) {
        val newList = mutableListOf<ComparedPerson>()
        newList.addAll(comparedPerson)
        val index = newList.indexOf(person)
        newList.remove(person)
        if (index != -1) newList.add(index, person.copy(name = newName))
        else newList.add(person.copy(name = newName))
        comparedPerson = newList
        _comparedPersonFlow.emit(comparedPerson)
    }

    suspend fun updatePersonHeight(person: ComparedPerson, heightCm: Int) {
        val newList = mutableListOf<ComparedPerson>()
        newList.addAll(comparedPerson)
        val index = newList.indexOf(person)
        newList.remove(person)
        if (index != -1) newList.add(index, person.copy(heightCm = heightCm))
        else newList.add(person.copy(heightCm = heightCm))
        comparedPerson = newList
        _comparedPersonFlow.emit(comparedPerson)
    }

}