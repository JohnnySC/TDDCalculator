package ru.easycode.tddcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(input: String, result: String, actions: MainActions) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = input,
            modifier = Modifier
                .testTag("input text")
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = result,
            modifier = Modifier
                .testTag("result text")
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(Modifier.weight(1f), "clear button", "C", actions::clearAll)
            CalculatorButton(Modifier.weight(1f), "backspace button", "x", actions::backspace)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(Modifier.weight(1f), "number 1 button", "1") { actions.input("1") }
            CalculatorButton(Modifier.weight(1f), "number 2 button", "2") { actions.input("2") }
            CalculatorButton(Modifier.weight(1f), "number 3 button", "3") { actions.input("3") }
            CalculatorButton(Modifier.weight(1f), "multiply button", "*", actions::multiply)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(Modifier.weight(1f), "number 4 button", "4") { actions.input("4") }
            CalculatorButton(Modifier.weight(1f), "number 5 button", "5") { actions.input("5") }
            CalculatorButton(Modifier.weight(1f), "number 6 button", "6") { actions.input("6") }
            CalculatorButton(Modifier.weight(1f), "divide button", "/", actions::divide)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(Modifier.weight(1f), "number 7 button", "7") { actions.input("7") }
            CalculatorButton(Modifier.weight(1f), "number 8 button", "8") { actions.input("8") }
            CalculatorButton(Modifier.weight(1f), "number 9 button", "9") { actions.input("9") }
            CalculatorButton(Modifier.weight(1f), "plus button", "+", actions::plus)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CalculatorButton(Modifier.weight(1f), "number zero button", "0", actions::inputZero)
            CalculatorButton(Modifier.weight(1f), "dot button", ".", actions::inputDot)
            CalculatorButton(Modifier.weight(1f), "minus button", "-", actions::minus)
            CalculatorButton(Modifier.weight(1f), "equals button", "=", actions::calculate)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(input = "1+2", result = "3", object : MainActions {
        override fun input(number: String) = Unit
        override fun inputDot() = Unit
        override fun plus() = Unit
        override fun minus() = Unit
        override fun multiply() = Unit
        override fun divide() = Unit
        override fun calculate() = Unit
        override fun inputZero() = Unit
        override fun backspace() = Unit
        override fun clearAll() = Unit
    })
}

@Composable
fun CalculatorButton(
    modifier: Modifier,
    testTag: String,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(4.dp)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}