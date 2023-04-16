package ua.scootersoft.heightcomparison.screens.heightcomparisons

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.ComparedPerson
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.ComparedPersonView
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.Gender
import ua.scootersoft.heightcomparison.screens.heightcomparisons.repository.HeightComparisonsRepository
import ua.scootersoft.heightcomparison.utils.Constants.TALLEST_DP
import javax.inject.Inject

@HiltViewModel
class HeightComparisonViewModel @Inject constructor(
    private val repository: HeightComparisonsRepository
) : ViewModel() {

    private val _comparedPeople = MutableStateFlow<List<ComparedPerson>>(emptyList())
    val comparedPeople = _comparedPeople.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getComparisonPeople().collect {
                _comparedPeople.emit(it)
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
        viewModelScope.launch {
            repository.updatePersonName(person, newName)
        }
    }

    fun updatePersonHeight(person: ComparedPerson, heightCm: Int) {
        viewModelScope.launch {
            repository.updatePersonHeight(person, heightCm)
        }
    }

    fun addPerson(name: String, imageUrl: String?, heightCm: Int, gender: Gender) {
        viewModelScope.launch {
            val person = ComparedPerson(name = name, imageUrl = imageUrl, heightCm = heightCm, gender = gender)
            repository.addPerson(person)
        }
    }

    fun removePerson(person: ComparedPerson) {
        viewModelScope.launch {
            repository.removePerson(person)
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