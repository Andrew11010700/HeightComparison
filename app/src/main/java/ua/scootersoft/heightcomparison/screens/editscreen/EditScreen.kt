package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.theartofdev.edmodo.cropper.CropImage
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.widgets.Toolbar
import ua.scootersoft.heightcomparison.utils.extensions.findActivity


@Composable
fun EditScreen(
    viewModel: HeightComparisonViewModel,
    navController: NavHostController
) {
    val comparedPeople by viewModel.comparedPeople.collectAsState()
    val checkedStates = viewModel.comparedPeople.collectAsState().value.map { mutableStateOf(it.isShowPerson) }

    Log.d("ComparedPerson", "EditScreen: people size = ${comparedPeople}, checked states = $checkedStates, size = ${checkedStates.size} ")
    val currentActivity = LocalContext.current.findActivity()

    Column {
        Toolbar(
            text = "Edit Person",
            leftImgRes = R.drawable.ic_back,
            onLeftImageAction = {
                navController.popBackStack()
            }
        )
        LazyColumn(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {
            itemsIndexed(comparedPeople.sortedBy { it.sortIndex }) { index, item ->
                val resultReadStorageLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            val resultData = CropImage.getActivityResult(result.data)
                            val resultUri = resultData.uri
                            Log.d("ImageUri", "EditScreen: On result = $resultUri")

                            if (resultUri != null)
                                viewModel.updatePersonImage(item, resultUri.toString())
                        }
                    }

                val galleryPermission =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                        when {
                            granted -> {
                                if (item.imageUrl.isNullOrBlank().not()) {
                                    resultReadStorageLauncher.launch(
                                        CropImage.activity(Uri.parse(item.imageUrl))
                                            .getIntent(currentActivity)
                                    )
                                } else {
                                    resultReadStorageLauncher.launch(
                                        CropImage
                                            .activity()
                                            .getIntent(currentActivity)
                                    )
                                }
                            }
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                currentActivity,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ).not() -> {
                                Log.d("ImageClick", "Please select again permission")
                            }
                            else -> Log.d("ImageClick", "NewPerson: permission not granted")
                        }
                    }

                checkedStates[index].value = item.isShowPerson

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
                        Modifier
                            .size(height = 350.dp, width = 150.dp)
                            .clickable {
                                Log.d("ImageUri", "EditScreen: uri = ${item.imageUrl}")
                                if (item.imageUrl != null) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        resultReadStorageLauncher.launch(
                                            CropImage
                                                .activity(Uri.parse(item.imageUrl))
                                                .getIntent(currentActivity)
                                        )
                                    } else {
                                        galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    }
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        resultReadStorageLauncher.launch(
                                            CropImage
                                                .activity()
                                                .getIntent(currentActivity)
                                        )
                                    } else {
                                        galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    }
                                }
                            }
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
                        Checkbox(checked = checkedStates[index].value, onCheckedChange = {
                            item.isShowPerson = item.isShowPerson.not()
                            viewModel.updatePerson(item)
                            checkedStates[index].value = item.isShowPerson
                        })
                        Button(onClick = {
                            viewModel.removePerson(item)
                        }) {
                            Text(text = "Remove this person")
                        }
                        Button(onClick = {
                            viewModel.removeImage(item)
                        }) {
                            Text(text = "Remove image")
                        }
                    }
                }
            }
        }
    }
}