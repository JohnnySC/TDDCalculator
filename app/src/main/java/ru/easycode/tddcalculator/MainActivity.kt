package ru.easycode.tddcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.easycode.tddcalculator.ui.theme.TDDCalculatorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TDDCalculatorTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            val viewModel: MainViewModel = viewModel()
            val inputFlow = viewModel.inputFlow.collectAsStateWithLifecycle()
            val resultFlow = viewModel.resultFlow.collectAsStateWithLifecycle()
            MainScreen(
                input = inputFlow.value,
                result = resultFlow.value,
                actions = viewModel
            )
        }
    }
}
