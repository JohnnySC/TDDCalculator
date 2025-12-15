package ru.easycode.tddcalculator

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * N: number (1 - 9)
 * 0: zero
 * M: Multiple repetitions
 * MAX: max value
 */
@RunWith(AndroidJUnit4::class)
class ScenarioUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val mainPage = MainPage(composeTestRule)

    //1. N + N = result
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

    //2. NM + NM = result
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

    //3. N_MAX + N_MAX = result
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

    //4. 0M + 0M = 0
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

    //5. 0M - 0M = 0
    @Test
    fun prevent_multiple_zeros_minus_operation() {
        repeat(10) {
            mainPage.clickNumberZero()
            mainPage.assertInputField(expected = "0")
        }
        mainPage.clickOperationMinusButton()
        mainPage.assertInputField(expected = "0-")
        repeat(10) {
            mainPage.clickNumberZero()
            mainPage.assertInputField(expected = "0-0")
        }
        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "0-0")
        mainPage.assertResult(expected = "0")
    }

    //6. 0 N + 0 N = result
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

    //7. - 0 N + N 0 = result
    @Test
    fun prevent_minus_zero() {
        mainPage.clickOperationMinusButton()
        mainPage.assertInputField(expected = "-")

        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "-")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "-1")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "-1+")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "-1+2")

        mainPage.clickNumberZero()
        mainPage.assertInputField(expected = "-1+20")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "-1+20")
        mainPage.assertResult("19")
    }

    //8. N +M N = result
    @Test
    fun prevent_multiple_plus_operations() {
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2")

        repeat(5) {
            mainPage.clickOperationPlusButton()
            mainPage.assertInputField(expected = "2+")
        }

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "2+1")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "2+1")
        mainPage.assertResult(expected = "3")
    }

    //9. N -M N = result
    @Test
    fun prevent_multiple_minus_operations() {
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2")

        repeat(5) {
            mainPage.clickOperationMinusButton()
            mainPage.assertInputField(expected = "2-")
        }

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "2-1")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "2-1")
        mainPage.assertResult(expected = "1")
    }

    //10. + N + N = result
    @Test
    fun prevent_leading_pluses() {
        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "1")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "1+")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "1+2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "1+2")
        mainPage.assertResult(expected = "3")
    }

    //11. N + N + NM + N = result
    @Test
    fun sum_of_more_than_two_numbers() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("1+")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1+2")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("3+")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("3+1")

        mainPage.clickNumberZero()
        mainPage.assertInputField("3+10")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("13+")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("13+2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("13+2")
        mainPage.assertResult("15")
    }

    //12. N - N - NM - N = result
    @Test
    fun diff_of_more_than_two_numbers() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("1-")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1-2")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-1-")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("-1-2")

        mainPage.clickNumberZero()
        mainPage.assertInputField("-1-20")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-21-")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("-21-2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("-21-2")
        mainPage.assertResult("-23")
    }

    //13. N + N = result + N = result + N + N = result
    @Test
    fun sum_after_equals() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("1+")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1+2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("1+2")
        mainPage.assertResult("3")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("3+")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("3+1")
        mainPage.assertResult("")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("3+1")
        mainPage.assertResult("4")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("4+")
        mainPage.assertResult("")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("4+2")
        mainPage.assertResult("")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("6+")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("6+1")
        mainPage.assertResult("")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("6+1")
        mainPage.assertResult("7")
    }

    //14. N - N = result - N = result - N - N = result
    @Test
    fun diff_after_equals() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("1-")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1-2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("1-2")
        mainPage.assertResult("-1")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-1-")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("-1-1")
        mainPage.assertResult("")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("-1-1")
        mainPage.assertResult("-2")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-2-")
        mainPage.assertResult("")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("-2-2")
        mainPage.assertResult("")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-4-")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("-4-1")
        mainPage.assertResult("")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("-4-1")
        mainPage.assertResult("-5")
    }

    //15. =M N =M + =M N =M result
    @Test
    fun prevent_equals_not_at_the_end() {
        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "")
            mainPage.assertResult("")
        }

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2")
        mainPage.assertResult("")

        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "2")
            mainPage.assertResult("")
        }

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField(expected = "2+")
        mainPage.assertResult("")

        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "2+")
            mainPage.assertResult("")
        }

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "2+1")
        mainPage.assertResult("")

        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "2+1")
            mainPage.assertResult("3")
        }
    }

    //16. =M N =M - =M N =M result
    @Test
    fun prevent_equals_after_minus() {
        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "")
            mainPage.assertResult("")
        }

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "2")
        mainPage.assertResult("")

        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "2")
            mainPage.assertResult("")
        }

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField(expected = "2-")
        mainPage.assertResult("")

        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "2-")
            mainPage.assertResult("")
        }

        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "2-1")
        mainPage.assertResult("")

        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField(expected = "2-1")
            mainPage.assertResult("1")
        }
    }

    //17. N - N = result
    @Test
    fun diff_of_two_numbers() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField(expected = "1")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField(expected = "1-")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField(expected = "1-2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField(expected = "1-2")
        mainPage.assertResult(expected = "-1")
    }

    //18. -M N - N = result
    @Test
    fun diff_sign_ahead() {
        repeat(3) {
            mainPage.clickOperationMinusButton()
            mainPage.assertInputField("-")
        }

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("-1")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-1-")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("-1-2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("-1-2")
        mainPage.assertResult("-3")
    }

    //19. -+ N -+ N -+ N = result
    @Test
    fun change_minus_to_plus() {
        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("-")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("2")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("2-")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("2+")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("2+1")

        mainPage.clickOperationMinusButton()
        mainPage.assertInputField("3-")
        mainPage.assertResult("")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("3+")
        mainPage.assertResult("")

        mainPage.clickNumberZero()
        mainPage.assertInputField("3+0")
        mainPage.assertResult("")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("3+0")
        mainPage.assertResult("3")
    }

    //20. *M 0M *M 0M =M result
    @Test
    fun multiply_zeros() {
        repeat(3) {
            mainPage.clickOperationMultiplyButton()
            mainPage.assertInputField("")
        }
        repeat(3) {
            mainPage.clickNumberZero()
            mainPage.assertInputField("0")
        }
        repeat(3) {
            mainPage.clickOperationMultiplyButton()
            mainPage.assertInputField("0*")
        }
        repeat(3) {
            mainPage.clickNumberZero()
            mainPage.assertInputField("0*0")
        }
        repeat(3) {
            mainPage.clickEqualsButton()
            mainPage.assertInputField("0*0")
            mainPage.assertResult("0")
        }
    }

    //21. NM * N = * NM =
    @Test
    fun multiply_several_times() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("11")

        mainPage.clickOperationMultiplyButton()
        mainPage.assertInputField("11*")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("11*2")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("11*2")
        mainPage.assertResult("22")

        mainPage.clickOperationMultiplyButton()
        mainPage.assertInputField("22*")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("22*1")

        mainPage.clickNumberZero()
        mainPage.assertInputField("22*10")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("22*10")
        mainPage.assertResult("220")
    }

    //22. NM * N += NM =
    @Test
    fun multiply_several_times_changed_operation() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("11")

        mainPage.clickOperationMultiplyButton()
        mainPage.assertInputField("11*")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("11*2")

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("22+")
        mainPage.assertResult("")

        mainPage.clickOperationMultiplyButton()
        mainPage.assertInputField("22*")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("22*1")

        mainPage.clickNumberZero()
        mainPage.assertInputField("22*10")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("22*10")
        mainPage.assertResult("220")
    }

    //23. divide a number by zero
    @Test
    fun divide_number_by_zero() {
        repeat(3) {
            mainPage.clickOperationDivideButton()
            mainPage.assertInputField("")
        }

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        repeat(3) {
            mainPage.clickOperationDivideButton()
            mainPage.assertInputField("1/")
        }

        repeat(3) {
            mainPage.clickNumberZero()
            mainPage.assertInputField("1/0")
        }

        mainPage.clickEqualsButton()
        mainPage.assertInputField("1/0")
        mainPage.assertResult("infinity")
    }

    @Test
    fun divide_by_zero_and_then_operation() {
        divide_number_by_zero()

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.assertResult("")
    }

    @Test
    fun divide_by_zero_and_then_number() {
        divide_number_by_zero()

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.assertResult("")
    }

    //102/12 = 8.5
    @Test
    fun divide_decimal() {
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.clickNumberZero()
        mainPage.assertInputField("10")
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("102")

        mainPage.clickOperationDivideButton()
        mainPage.assertInputField("102/")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("102/1")

        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("102/12")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("102/12")
        mainPage.assertResult("8.5")
    }

    @Test
    fun sum_of_decimals() {
        divide_decimal()

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("8.5+")
        mainPage.clickNumberOneButton()
        mainPage.assertInputField("8.5+1")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("8.5+1")
        mainPage.assertResult("9.5")
    }

    @Test
    fun uncertainty() {
        mainPage.clickNumberZero()
        mainPage.assertInputField("0")

        mainPage.clickOperationDivideButton()
        mainPage.assertInputField("0/")

        mainPage.clickNumberZero()
        mainPage.assertInputField("0/0")

        mainPage.clickEqualsButton()
        mainPage.assertInputField("0/0")
        mainPage.assertResult("uncertainty")
    }

    @Test
    fun uncertainty_and_then_operation() {
        uncertainty()

        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.assertResult("")
    }

    @Test
    fun uncertainty_and_then_number() {
        uncertainty()

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.assertResult("")
    }

    @Test
    fun clear_all() {
        mainPage.clickClearAll()

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")

        mainPage.clickClearAll()
        mainPage.assertInputField("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("1+")

        mainPage.clickClearAll()
        mainPage.assertInputField("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("1+")
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1+2")

        mainPage.clickClearAll()
        mainPage.assertInputField("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("1+")
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1+2")
        mainPage.clickEqualsButton()
        mainPage.assertInputField("1+2")
        mainPage.assertResult("3")

        mainPage.clickClearAll()
        mainPage.assertInputField("")
        mainPage.assertResult("")

        mainPage.clickNumberOneButton()
        mainPage.assertInputField("1")
        mainPage.clickOperationPlusButton()
        mainPage.assertInputField("1+")
        mainPage.clickNumberTwoButton()
        mainPage.assertInputField("1+2")
        mainPage.clickEqualsButton()
        mainPage.assertInputField("1+2")
        mainPage.assertResult("3")
    }
}