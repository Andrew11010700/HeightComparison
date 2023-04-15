package ua.scootersoft.heightcomparison.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.scootersoft.heightcomparison.screens.heightcomparisons.EditScreen
import ua.scootersoft.heightcomparison.screens.heightcomparisons.HeightComparisonViewModel
import ua.scootersoft.heightcomparison.screens.heightcomparisons.NewPerson
import ua.scootersoft.heightcomparison.ui.theme.HeightComparisonTheme

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel: HeightComparisonViewModel = hiltViewModel()

            HeightComparisonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   NavHost(
                       navController = navController,
                       startDestination = "heightComparisonScreen"
                   ) {
                       composable("heightComparisonScreen") {
                           HeightComparisonScreen(viewModel, navController)
                       }
                       composable("editPersonScreen") {
                           EditScreen(viewModel)
                       }
                       composable("addNewPerson") {
                           NewPerson(viewModel)
                       }
                   }
                }
            }
          
        }
    }
}
