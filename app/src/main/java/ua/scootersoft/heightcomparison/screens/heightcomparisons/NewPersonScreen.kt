package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.Manifest
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.theartofdev.edmodo.cropper.CropImage
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.screens.heightcomparisons.model.Gender
import ua.scootersoft.heightcomparison.utils.Constants.TALLEST_DP
import ua.scootersoft.heightcomparison.utils.extensions.findActivity

@ExperimentalMaterialApi
@Composable
fun NewPerson(
    viewModel: HeightComparisonViewModel,
    navController: NavController
) {

    val nameState = remember { mutableStateOf("Name") }
    val heightCmState = remember { mutableStateOf(170) }
    val spinnerExpandedState = remember { mutableStateOf(false) }
    val selectedGender = remember { mutableStateOf(Gender.MAN) }
    val selectedUri = remember { mutableStateOf<String?>(null) }

    val resultReadStorageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultData = CropImage.getActivityResult(result.data)
                val resultUri = resultData.uri
                selectedUri.value = resultUri.toString()
            }
        }

    val currentActivity = LocalContext.current.findActivity()

    val galleryPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    resultReadStorageLauncher.launch(CropImage.activity().getIntent(currentActivity))
                }
                shouldShowRequestPermissionRationale(currentActivity, Manifest.permission.READ_EXTERNAL_STORAGE).not() -> {
                    Log.d("ImageClick", "Please select again permission")
                }
                else -> Log.d("ImageClick", "NewPerson: permission not granted")
            }
        }

    val image = if (selectedUri.value.isNullOrBlank())
        painterResource(id = R.drawable.ic_baseline_add_24)
    else
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Uri.parse(selectedUri.value))
                .size(Size.ORIGINAL)
                .build()
        )

    LazyColumn(modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp)) {
        item {
            TextField(value = nameState.value, onValueChange = {
                nameState.value = it
            })
        }
        item {
            Image(
                painter = image,
                contentDescription = "Add image",
                modifier = if (selectedUri.value.isNullOrBlank()) {
                    Modifier
                        .fillMaxWidth()
                        .height(TALLEST_DP)
                        .clickable {
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
                } else {
                    Modifier
                        .clickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                resultReadStorageLauncher.launch(CropImage.activity().getIntent(currentActivity))
                            } else {
                                galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
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
                viewModel.addPerson(nameState.value, selectedUri.value, heightCmState.value, selectedGender.value)
                navController.popBackStack()
            }) {
                Text(text = "Add person")
            }
        }
    }
}