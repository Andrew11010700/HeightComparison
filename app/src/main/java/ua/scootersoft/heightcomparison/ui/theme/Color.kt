package ua.scootersoft.heightcomparison.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryBackgroundLight = Color(0xFFF3F4F8)

val PrimaryDark = Color(0xFFF3F4F8)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val PrimaryLight = Color(0xFFE5F3E5)
val PurpleGrey40 = Color(0xFF03A9F4)
val Pink40 = Color(0xFFFF5722)

val OnSurfaceLight = Color(0xFF1C1B1F)

val OnPrimaryLight = Color(0xFF4CAF50)
val OnPrimaryDark = Color(0xFF7EF083)

@Composable
fun outlinedTextColorsStyle(darkTheme: Boolean = isSystemInDarkTheme()) = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = if (darkTheme) PrimaryDark else OnSurfaceLight,
    focusedLabelColor =  if (darkTheme) PrimaryDark else OnSurfaceLight,
    cursorColor = if (darkTheme) PrimaryDark else OnSurfaceLight,
    textColor = if (darkTheme) PrimaryDark else OnSurfaceLight,
    unfocusedLabelColor = if (darkTheme) PrimaryDark else OnSurfaceLight,
    unfocusedBorderColor = if (darkTheme) PrimaryDark else OnSurfaceLight
)

@Composable
fun buttonColorsStyle(darkTheme: Boolean = isSystemInDarkTheme()) = ButtonDefaults.buttonColors(
    backgroundColor = if (darkTheme) OnPrimaryDark else OnPrimaryLight,
    contentColor = if (darkTheme) OnSurfaceLight else PrimaryLight
)