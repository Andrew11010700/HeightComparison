package ua.scootersoft.heightcomparison.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ua.scootersoft.heightcomparison.screens.heightcomparisons.EditScreen
import ua.scootersoft.heightcomparison.screens.heightcomparisons.HeightComparisonViewModel

@Composable
fun HeightComparisonScreen(
    viewModel: HeightComparisonViewModel,
    navController: NavHostController
) {
    val comparedPeople by viewModel.comparedPeople.collectAsState()
    LazyColumn(modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp)) {

        item {
            LazyRow {
                items(items = comparedPeople.filter { it.isShowPerson }) { item ->
                    val image = if (item.imageUrl.isNullOrBlank())
                        painterResource(id = item.defaultImage)
                    else
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(Uri.parse(item.imageUrl))
                                .size(Size.ORIGINAL)
                                .build()
                        )

                    val imageSize = image.intrinsicSize

                    val tallestPerson = comparedPeople.maxByOrNull { it.heightCm }
                    tallestPerson ?: return@items

                    val itemView = viewModel.convertToComparedPersonView(item, imageSize, tallestPerson)

                    Image(
                        painter = image,
                        contentDescription = itemView.name,
                        modifier = Modifier
                            .padding(top = itemView.marginTopDp)
                            .size(width = itemView.imageWidthDp, height = itemView.imageHeightDp)
                            .border(BorderStroke(1.dp, Black))
                            .background(Yellow),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        item {
            LazyRow(modifier = Modifier.padding(top = 10.dp)) {
                item {
                    Button(
                        onClick = {
                            navController.navigate("addNewPerson")
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        )
                    ) {
                        Text(text = "Add Person")
                    }
                }
                item {
                    Button(
                        modifier = Modifier.padding(start = 10.dp),
                        onClick = {
                            navController.navigate("editPersonScreen")
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        )
                    ) {
                        Text(text = "Edit Person")
                    }
                }

            }

        }

    }
}