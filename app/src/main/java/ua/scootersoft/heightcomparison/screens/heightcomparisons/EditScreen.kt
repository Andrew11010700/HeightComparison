package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.R.attr.label
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun EditScreen(viewModel: HeightComparisonViewModel) {
    val comparedPeople by viewModel.comparedPeople.collectAsState()
    Log.d("StateValue", "EditScreen: state = $comparedPeople")
    LazyColumn(modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp)) {
        items(comparedPeople) { item ->
            Row {
                Image(
                    painter = painterResource(id = item.defaultImage),
                    contentDescription = item.name
                )
                Column {
                    TextField(
                        value = item.name,
                        onValueChange = {
                            viewModel.updatePersonName(item, it)
                        }
                    )
                    TextField(
                        value = item.heightCm.toString(),
                        onValueChange = {
                            viewModel.updatePersonHeight(item, it.toInt())
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }
    }
}