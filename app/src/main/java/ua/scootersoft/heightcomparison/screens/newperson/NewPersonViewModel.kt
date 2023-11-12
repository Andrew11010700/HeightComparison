package ua.scootersoft.heightcomparison.screens.newperson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.scootersoft.heightcomparison.model.ComparedPerson
import ua.scootersoft.heightcomparison.model.Gender
import ua.scootersoft.heightcomparison.repository.HeightComparisonsRepository
import javax.inject.Inject

@HiltViewModel
class NewPersonViewModel @Inject constructor(
    private val repository: HeightComparisonsRepository
) : ViewModel() {

    private val _nameState = MutableStateFlow("")
    val nameState = _nameState.asStateFlow()

    private val _heightCmState = MutableStateFlow(0)
    val heightCmState = _heightCmState.asStateFlow()

    private val _spinnerExpandState = MutableStateFlow(false)
    val spinnerExpandState = _spinnerExpandState.asStateFlow()

    private val _selectedGenderState = MutableStateFlow(Gender.MAN)
    val selectedGenderState = _selectedGenderState.asStateFlow()

    private val _selectedUriState = MutableStateFlow<String?>(null)
    val selectedUriState = _selectedUriState.asStateFlow()

    fun addPerson(name: String, imageUrl: String?, heightCm: Int, gender: Gender) {
        viewModelScope.launch {
            val person = ComparedPerson(name = name, imageUrl = imageUrl, heightCm = heightCm, gender = gender)
            repository.addPerson(person)
        }
    }

    fun updateUriState(uri: String?) {
        _selectedUriState.value = uri
    }

    fun updateNameState(name: String) {
        _nameState.value = name
    }

    fun updateHeightState(heightCm: Int) {
        _heightCmState.value = heightCm
    }

    fun updateSpinnerValueState(expand: Boolean) {
        _spinnerExpandState.value = expand
    }

    fun updateGenderState(gender: Gender) {
        _selectedGenderState.value = gender
    }

}