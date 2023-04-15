package ua.scootersoft.heightcomparison.screens.heightcomparisons.model

import androidx.compose.ui.unit.Dp

data class ComparedPersonView(
    val name: String,
    val heightCm: Int,
    val imageRes: Int,
    val imageHeightDp: Dp,
    val imageWidthDp: Dp,
    val marginTopDp: Dp
)
