package ua.scootersoft.heightcomparison.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ua.scootersoft.heightcomparison.screens.heightcomparisons.HeightComparisonViewModel

@Composable
fun HeightComparisonScreen(
    viewModel: HeightComparisonViewModel,
    navController: NavHostController
) {
    val comparedPeople by viewModel.comparedPeople.collectAsState()
    val offetsStates = remember { comparedPeople.map { mutableStateOf(Offset(0f, 0f)) }}
    val lazyListState = rememberLazyListState()
    val currentIndexOffset = remember { mutableStateOf(0) }

    val draggableEnd = {
        val currentOffset = offetsStates[currentIndexOffset.value].value

        val nextPosition = if (currentOffset.x.dp > 20.dp) {
            currentIndexOffset.value + 1
        } else if (currentOffset.x.dp < -20.dp) {
            currentIndexOffset.value - 1
        } else {
            currentIndexOffset.value
        }

        if (nextPosition >= 0 && nextPosition < comparedPeople.size &&
            nextPosition != currentIndexOffset.value) {
            viewModel.swapElements(currentIndexOffset.value, nextPosition)
        }

        offetsStates[currentIndexOffset.value].value = currentOffset.copy(x = 0f)
    }

    LazyColumn(modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp)) {

        item {
            LazyRow(Modifier.pointerInput(Unit) {
                detectDragGesturesAfterLongPress (
                    onDrag = { change, offset ->
                        change.consume()
                        val x = offset.x
                        val currentOffset = offetsStates[currentIndexOffset.value].value
                        offetsStates[currentIndexOffset.value].value = currentOffset.copy(x = currentOffset.x + x)
                    },
                    onDragStart = { offset ->
                        lazyListState.layoutInfo.visibleItemsInfo
                            .firstOrNull { item -> offset.x.toInt() in item.offset..(item.offset + item.size) }
                            ?.also {
                                currentIndexOffset.value = it.index
                            }
                    },
                    onDragEnd = {
                        draggableEnd()
                    },
                    onDragCancel = {
                        draggableEnd()
                    }
                )
            },
                state = lazyListState) {
                itemsIndexed(items = comparedPeople.filter { it.isShowPerson }) { index, item ->
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
                    tallestPerson ?: return@itemsIndexed

                    val itemView = viewModel.convertToComparedPersonView(item, imageSize, tallestPerson)

                    Image(
                        painter = image,
                        contentDescription = itemView.name,
                        modifier = Modifier
                            .padding(top = itemView.marginTopDp)
                            .size(width = itemView.imageWidthDp, height = itemView.imageHeightDp)
                            .border(BorderStroke(1.dp, Black))
                            .background(Yellow)
                            .clickable {
                                Log.d("Positions", "HeightComparisonScreen: CLICK IMAGE = $item ")
                            }
                            .offset(
                                x = offetsStates[index].value.x.dp,
                                y = offetsStates[index].value.y.dp
                            ),
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