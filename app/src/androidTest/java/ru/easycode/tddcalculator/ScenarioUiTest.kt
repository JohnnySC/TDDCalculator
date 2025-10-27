package ru.easycode.tddcalculator

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val mainPage = MainPage(composeTestRule)

    @Test
    fun sum_of_two_numbers() {
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "2+")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "2+1")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "2+1")
        mainPage.assertResult(expected = "3")
    }

    @Test
    fun sum_of_two_numbers_more_complex() {
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "21")

        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "210")

        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "2100")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "2100+")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "2100+1")

        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "2100+10")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2100+102")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "2100+102")
        mainPage.assertResult(expected = "2202")
    }

    @Test
    fun sum_of_two_numbers_corner_case() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "1")

        var expected = "1"
        repeat(9) {
            mainPage.clickNumberZero()
            expected += "0"
            mainPage.assertInputField(expected = expected)
        }

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "1000000000+")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "1000000000+2")

        expected = "1000000000+2"
        repeat(9) {
            mainPage.clickNumberZero()
            expected += "0"
            mainPage.assertInputField(expected = expected)
        }

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "1000000000+2000000000")
        mainPage.assertResult(expected = "3000000000")
    }

    @Test
    fun prevent_multiple_zeros() {
        repeat(10) {
            mainPage.clickNumberZero()
            mainPage.assertInputField(expected = "0")
        }
        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "0+")
        repeat(10) {
            mainPage.clickNumberZero()
            mainPage.assertInputField(expected = "0+0")
        }
        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "0+0")
        mainPage.assertResult(expected = "0")
    }

    @Test
    fun prevent_leading_zeros() {
        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "0")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "1")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "1+")

        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "1+0")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "1+2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "1+2")
        mainPage.assertResult(expected = "3")
    }
}