package ua.scootersoft.heightcomparison.model

import androidx.compose.ui.unit.Dp

data class ComparedPersonView(
    val name: String,
    val heightCm: Int,
    val imageRes: Int,
    val imageHeightDp: Dp,
    val imageWidthDp: Dp,
    val marginTopDp: Dp
)
