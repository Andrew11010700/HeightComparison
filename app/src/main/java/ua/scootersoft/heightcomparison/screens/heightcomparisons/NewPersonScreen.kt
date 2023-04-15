package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.Gender
import ua.scootersoft.heightcomparison.utils.Constants.TALLEST_DP

@ExperimentalMaterialApi
@Composable
fun NewPerson(viewModel: HeightComparisonViewModel) {
    val nameState = remember { mutableStateOf("Name") }
    val heightCmState = remember { mutableStateOf(170) }
    val spinnerExpandedState = remember { mutableStateOf(false) }
    var selectedGender = remember { mutableStateOf(Gender.MAN) }

    LazyColumn(modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp)) {
        item {
            TextField(value = nameState.value, onValueChange = {
                nameState.value = it
            })
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "Add image",
                modifier = Modifier
                    .size(TALLEST_DP)
                    .clickable {
                        Log.d("ImageClick", "NewPerson: click on image")
                    },
                contentScale = ContentScale.FillBounds,
            )
        }
        item {
            TextField(
                value = heightCmState.value.toString(),
                onValueChange = {
                    heightCmState.value = it.toInt()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        item {
            ExposedDropdownMenuBox(expanded = spinnerExpandedState.value, onExpandedChange = {
                spinnerExpandedState.value = spinnerExpandedState.value.not()
            }) {
                TextField(
                    readOnly = true,
                    value = selectedGender.value.name,
                    onValueChange = { },
                    label = { Text("Select gender") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = spinnerExpandedState.value
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(expanded = spinnerExpandedState.value, onDismissRequest = {
                    spinnerExpandedState.value = false
                }) {
                    Gender.values().forEach { gender ->
                        DropdownMenuItem(onClick = {
                            selectedGender.value = gender
                            spinnerExpandedState.value = false
                        }) {
                            Text(text = gender.name)
                        }
                    }
                }
            }
        }
        item {
            Button(onClick = {
                Log.d("ImageClick", "Add person click")
            }) {
                Text(text = "Add person")
            }
        }
    }
}