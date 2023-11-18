package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.util.Log
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.scootersoft.heightcomparison.model.ComparedPerson
import ua.scootersoft.heightcomparison.model.ComparedPersonView
import ua.scootersoft.heightcomparison.model.Gender
import ua.scootersoft.heightcomparison.repository.HeightComparisonsRepository
import ua.scootersoft.heightcomparison.utils.Constants.TALLEST_DP
import javax.inject.Inject

@HiltViewModel
class HeightComparisonViewModel @Inject constructor(
    private val repository: HeightComparisonsRepository
) : ViewModel() {

    private val _comparedPeople = MutableStateFlow<List<ComparedPerson>>(emptyList())
    val comparedPeople = _comparedPeople.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.getComparisonPeople().collect {
                _comparedPeople.emit(it)
            }
        }

        viewModelScope.launch(context = Dispatchers.IO) {
            if (repository.isPersonsEmpty()) {
                val peopleList = mutableListOf<ComparedPerson>()
                peopleList.addAll(listOf(
                    ComparedPerson(imageUrl = null, gender = Gender.WOMAN, name = "Average girl", heightCm = 170),
                    ComparedPerson(imageUrl = null, gender = Gender.MAN,  name = "Andrew", heightCm = 160),
                    ComparedPerson(imageUrl = null, gender = Gender.WOMAN,name = "Tall girl", heightCm = 179)
                ))
                val ids = repository.insertAll(peopleList)
                val itemsFromDb = repository.getVisibleItems()
                Log.d("ComparedPerson", "ids = $ids")

                ids.forEachIndexed { index, id ->
                    itemsFromDb[index].sortIndex  = id
                }

                repository.updateAll(itemsFromDb)
            }
        }

    }

    fun convertToComparedPersonView(
        currentPerson: ComparedPerson,
        imageSize: Size,
        tallestPerson: ComparedPerson
    ): ComparedPersonView {
        val heightDifferance = tallestPerson.heightCm - currentPerson.heightCm

        val tallestRatio = if (heightDifferance != 0)
            tallestPerson.heightCm / heightDifferance.toFloat()
        else
            0f

        val personRatio = if (tallestRatio != 0f)
            TALLEST_DP / tallestRatio
        else
            0.dp


        val viewHeight = if (personRatio != 0.dp)
            TALLEST_DP - personRatio
        else
            TALLEST_DP

        val safetySize = getImageSize(imageSize)
        val imageHeight = safetySize.height
        val imageWidth = safetySize.width
        val imageRatio =
            if (imageHeight > imageWidth) imageHeight / imageWidth else imageWidth / imageHeight

        val viewWidth =
            if (imageHeight > imageWidth) viewHeight / imageRatio else viewHeight * imageRatio

        return ComparedPersonView(
            currentPerson.name,
            currentPerson.heightCm,
            currentPerson.defaultImage,
            viewHeight,
            viewWidth,
            personRatio
        )
    }

    fun updatePersonName(person: ComparedPerson, newName: String) {
        viewModelScope.launch(context = Dispatchers.IO)  {
            repository.updatePersonName(person, newName)
        }
    }

    fun updatePersonHeight(person: ComparedPerson, heightCm: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.updatePersonHeight(person, heightCm)
        }
    }

    fun updatePersonImage(person: ComparedPerson, imageUrl: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.updatePersonImage(person, imageUrl)
        }
    }

    fun removePerson(person: ComparedPerson) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.removePerson(person)
        }
    }

    fun removeImage(person: ComparedPerson) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.removeImage(person)
        }
    }

    fun updatePerson(person: ComparedPerson) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.updatePerson(person)
        }
    }

    fun swapElements(currentIndex: Int, nextIndex: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.swapPositions(currentIndex, nextIndex)
        }
    }

    private fun getImageSize(imageSize: Size): Size {
        return try {
            val imageHeight = imageSize.height
            val imageWidth = imageSize.width
            Size(imageWidth, imageHeight)
        } catch (e: Throwable) {
            Size(300f, 750f)
        }
    }

}