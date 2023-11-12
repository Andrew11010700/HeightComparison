package ua.scootersoft.heightcomparison.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.screens.heightcomparisons.HeightComparisonViewModel
import ua.scootersoft.heightcomparison.widgets.Toolbar

@Composable
fun HeightComparisonScreen(
    viewModel: HeightComparisonViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val comparedPeople by viewModel.comparedPeople.collectAsState()
    val offsetsStates = remember { comparedPeople.map { mutableStateOf(Offset(0f, 0f)) } }
    val lazyListState = rememberLazyListState()
    val currentIndexOffset = remember { mutableStateOf(0) }

    val draggableEnd = {
        val currentOffset = offsetsStates[currentIndexOffset.value].value

        val nextPosition = if (currentOffset.x.dp > 20.dp) {
            currentIndexOffset.value + 1
        } else if (currentOffset.x.dp < -20.dp) {
            currentIndexOffset.value - 1
        } else {
            currentIndexOffset.value
        }

        if (nextPosition >= 0 && nextPosition < comparedPeople.size &&
            nextPosition != currentIndexOffset.value
        ) {
            viewModel.swapElements(currentIndexOffset.value, nextPosition)
        }

        offsetsStates[currentIndexOffset.value].value = currentOffset.copy(x = 0f)
    }

    Column {

        Toolbar(
            text = "Compare persons",
            leftImgRes = R.drawable.ic_add_person,
            rightImgRes = R.drawable.ic_edit_person,
            onLeftImageAction = {
                navController.navigate("addNewPerson")
            },
            onRightImageAction = {
                navController.navigate("editPersonScreen")
            }
        )

        LazyRow(
            Modifier
                .verticalScroll(scrollState)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDrag = { change, offset ->
                            change.consume()
                            val x = offset.x
                            val currentOffset = offsetsStates[currentIndexOffset.value].value
                            offsetsStates[currentIndexOffset.value].value =
                                currentOffset.copy(x = currentOffset.x + x)
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
                }
                .padding(top = 24.dp),
            state = lazyListState
        ) {
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

                Column {
                    Text(
                        text = itemView.name,
                        modifier = Modifier
                            .width(itemView.imageWidthDp),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                    Image(
                        painter = image,
                        contentDescription = itemView.name,
                        modifier = Modifier
                            .padding(top = itemView.marginTopDp)
                            .size(width = itemView.imageWidthDp, height = itemView.imageHeightDp)
                            .border(BorderStroke(1.dp, Black))
                            .clickable {
                                Log.d("Positions", "HeightComparisonScreen: CLICK IMAGE = $item ")
                            }
                            .offset(
                                x = offsetsStates[index].value.x.dp,
                                y = offsetsStates[index].value.y.dp
                            ),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = "${itemView.heightCm} cm",
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .width(itemView.imageWidthDp)
                    )
                }
            }
        }

    }
}