package ru.easycode.tddcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.easycode.tddcalculator.ui.theme.TDDCalculatorTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TDDCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
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
        }
    }
}

