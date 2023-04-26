package ua.scootersoft.heightcomparison.screens.heightcomparisons.model

import ua.scootersoft.heightcomparison.R

data class ComparedPerson(
    val id: Long = 0,
    val imageUrl: String? = null,
    val gender: Gender,
    val name: String = "",
    val heightCm: Int = 170,
    var isShowPerson: Boolean = true,
    val defaultImage: Int = if (gender == Gender.MAN) R.drawable.default_man else R.drawable.default_woman
)