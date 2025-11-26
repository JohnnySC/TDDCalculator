package ru.easycode.tddcalculator

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.performClick

class MainPage(composeTestRule: ComposeContentTestRule) {

    private val numberZeroButton =
        composeTestRule.onNode(
            hasTestTag("number zero button") and
                    hasText("0") and
                    hasClickAction()
        )

    private val numberOneButton =
        composeTestRule.onNode(
            hasTestTag("number one button") and
                    hasText("1") and
                    hasClickAction()
        )

    private val numberTwoButton =
        composeTestRule.onNode(
            hasTestTag("number two button") and
                    hasText("2") and
                    hasClickAction()
        )

    private val equalsButton =
        composeTestRule.onNode(
            hasTestTag("equals button") and
                    hasText("=") and
                    hasClickAction()
        )

    private val plusButton =
        composeTestRule.onNode(
            hasTestTag("plus button") and
                    hasText("+") and
                    hasClickAction()
        )

    private val minusButton =
        composeTestRule.onNode(
            hasTestTag("minus button") and
                    hasText("-") and
                    hasClickAction()
        )

    private val inputText = composeTestRule.onNode(
        hasTestTag("input text") and
                hasNoClickAction()
    )

    private val resultText = composeTestRule.onNode(
        hasTestTag("result text") and
                hasNoClickAction()
    )

    fun clickNumberOneButton() {
        numberOneButton.performClick()
    }

    fun assertInputField(expected: String) {
        inputText.assertTextEquals(expected)
    }

    fun clickOperationPlusButton() {
        plusButton.performClick()
    }

    fun clickOperationMinusButton() {
        minusButton.performClick()
    }

    fun clickNumberTwoButton() {
        numberTwoButton.performClick()
    }

    fun clickEqualsButton() {
        equalsButton.performClick()
    }

    fun assertResult(expected: String) {
        resultText.assertTextEquals(expected)
    }

    fun clickNumberZero() {
        numberZeroButton.performClick()
    }
}