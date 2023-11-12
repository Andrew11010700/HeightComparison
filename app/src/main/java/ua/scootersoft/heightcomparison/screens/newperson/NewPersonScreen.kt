package ua.scootersoft.heightcomparison.screens.heightcomparisons

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.theartofdev.edmodo.cropper.CropImage
import ua.scootersoft.heightcomparison.R
import ua.scootersoft.heightcomparison.model.Gender
import ua.scootersoft.heightcomparison.screens.newperson.NewPersonViewModel
import ua.scootersoft.heightcomparison.widgets.Toolbar
import ua.scootersoft.heightcomparison.ui.theme.buttonColorsStyle
import ua.scootersoft.heightcomparison.ui.theme.outlinedTextColorsStyle
import ua.scootersoft.heightcomparison.utils.Constants.TALLEST_DP
import ua.scootersoft.heightcomparison.utils.UriPathHelper
import ua.scootersoft.heightcomparison.utils.extensions.findActivity

@ExperimentalMaterialApi
@Composable
fun NewPerson(
    viewModel: NewPersonViewModel = hiltViewModel(),
    navController: NavController
) {

    val scrollState = rememberScrollState()
    val nameState = viewModel.nameState.collectAsState()
    val heightCmState = viewModel.heightCmState.collectAsState()
    val spinnerExpandedState = viewModel.spinnerExpandState.collectAsState()
    val selectedGender = viewModel.selectedGenderState.collectAsState()
    val selectedUri = viewModel.selectedUriState.collectAsState()

    val resultReadStorageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultData = CropImage.getActivityResult(result.data)
                val resultUri = resultData.uri
                viewModel.updateUriState(resultUri.toString())
            }
        }

    val currentActivity = LocalContext.current.findActivity()

    val requestOpenImage =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let { uri ->
                Log.d("ResultGg", "NewPerson: ${uri}")
                resultReadStorageLauncher.launch(
                    CropImage
                        .activity(
                            uri
                        )
                        .getIntent(currentActivity)
                )
//                UriPathHelper.getPath(context, uri).let { path ->
//                    Log.d("ResultGg", "NewPerson: file://$path")
//                    resultReadStorageLauncher.launch(
//                        CropImage
//                            .activity(
//                                Uri.parse(
//                                    "file://$path"
//                                )
//                            )
//                            .getIntent(currentActivity)
//                    )
//                }
            }
        }

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

    Column {
        Toolbar(
            text = "Add new person",
            leftImgRes = R.drawable.ic_back,
            onLeftImageAction = {
                navController.popBackStack()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(top = 20.dp, start = 24.dp, end = 24.dp)
        ) {

            OutlinedTextField(
                value = nameState.value,
                label = { Text(text = "Name")},
                onValueChange = {
                    viewModel.updateNameState(it)
                },
                shape = RoundedCornerShape(8.dp),
                colors = outlinedTextColorsStyle()
            )

            Image(
                painter = image,
                contentDescription = "Add image",
                modifier = if (selectedUri.value.isNullOrBlank()) {
                    Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(TALLEST_DP)
                        .clickable {
                            //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                            resultReadStorageLauncher.launch(
//                                CropImage
//                                    .activity()
//                                    .getIntent(currentActivity)
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            requestOpenImage.launch(
                                Intent.createChooser(
                                    intent,
                                    "Select Picture"
                                )
                            )

//                            } else {
//                                galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                            }
                        }
                } else {
                    Modifier
                        .padding(top = 10.dp)
                        .clickable {

                                resultReadStorageLauncher.launch(
                                    CropImage
                                        .activity(
                                            Uri.parse(
                                                selectedUri.value
                                            )
                                        )
                                        .getIntent(currentActivity)
                                )

                        }
                },
                contentScale = ContentScale.FillBounds,
            )

            if (selectedUri.value.isNullOrBlank().not()) {
                Button(
                    modifier = Modifier.padding(top = 4.dp),
                    onClick = {
                        viewModel.updateUriState(null)
                    },
                    colors = buttonColorsStyle()
                ) {
                    Text(text = "Remove image")
                }
            }

            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = heightCmState.value.toString(),
                label = { Text(text = "Height in cm")},
                onValueChange = {
                    viewModel.updateHeightState(try { it.toInt() } catch (e: Throwable) { 0 })
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = outlinedTextColorsStyle()
            )

            ExposedDropdownMenuBox(
                modifier = Modifier.padding(top = 10.dp),
                expanded = spinnerExpandedState.value, onExpandedChange = {
                    viewModel.updateSpinnerValueState(spinnerExpandedState.value.not())
            }) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedGender.value.name,
                    onValueChange = { },
                    label = { Text("Select gender") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = spinnerExpandedState.value
                        )
                    },
                    colors = outlinedTextColorsStyle()
                )
                ExposedDropdownMenu(expanded = spinnerExpandedState.value, onDismissRequest = {
                    viewModel.updateSpinnerValueState(  false)
                }) {
                    Gender.values().forEach { gender ->
                        DropdownMenuItem(onClick = {
                            viewModel.updateGenderState(gender)
                            viewModel.updateSpinnerValueState( false)
                        }) {
                            Text(text = gender.name)
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 20.dp)
                    .align(Alignment.CenterHorizontally),
                colors = buttonColorsStyle(),
                onClick = {
                viewModel.addPerson(
                    nameState.value,
                    selectedUri.value,
                    heightCmState.value,
                    selectedGender.value
                )
                navController.popBackStack()
            }) {
                Text(text = "Add person")
            }
        }
    }
}