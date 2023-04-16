package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size


@Composable
fun EditScreen(viewModel: HeightComparisonViewModel) {
    val comparedPeople by viewModel.comparedPeople.collectAsState()
    Log.d("StateValue", "EditScreen: state = $comparedPeople")
    LazyColumn(modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp)) {
        items(comparedPeople) { item ->
            val image = if (item.imageUrl.isNullOrBlank())
                painterResource(id = item.defaultImage)
            else
                rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Uri.parse(item.imageUrl))
                        .size(Size.ORIGINAL)
                        .build()
                )

            Row {
                Image(
                    painter = image,
                    contentDescription = item.name,
                    Modifier.size(height = 350.dp, width = 150.dp)
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
                    Button(onClick = {
                        viewModel.removePerson(item)
                    }) {
                        Text(text = "Remove this person")
                    }
                }
            }
        }
    }
}