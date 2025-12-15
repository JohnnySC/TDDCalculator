package ru.easycode.tddcalculator

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

        Row {
            CalculatorButton("number one button", "1", actions::inputOne)
            CalculatorButton("number two button", "2", actions::inputTwo)
            CalculatorButton("number zero button", "0", actions::inputZero)
            CalculatorButton("backspace button", "x", actions::backspace)
        }
        Row {
            CalculatorButton("plus button", "+", actions::plus)
            CalculatorButton("minus button", "-", actions::minus)
            CalculatorButton("multiply button", "*", actions::multiply)
            CalculatorButton("divide button", "/", actions::divide)
            CalculatorButton("equals button", "=", actions::calculate)
            CalculatorButton("clear button", "C", actions::clearAll)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(input = "1+2", result = "3", object : MainActions {
        override fun inputOne() = Unit
        override fun inputTwo() = Unit
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
    testTag: String,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}