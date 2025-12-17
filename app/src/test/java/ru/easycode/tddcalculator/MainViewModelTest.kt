package ru.easycode.tddcalculator

import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var repository: MainRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var inputFlow: StateFlow<String>
    private lateinit var resultFlow: StateFlow<String>

    @Before
    fun setup() {
        repository = MainRepository.Base()
        viewModel = MainViewModel(repository = repository)
        inputFlow = viewModel.inputFlow
        resultFlow = viewModel.resultFlow
        assertEquals("", inputFlow.value)
        assertEquals("", resultFlow.value)
    }

    @Test
    fun sum_of_two_numbers() = with(viewModel) {
        input("2")
        assertInputField(expected = "2")

        plus()
        assertInputField(expected = "2+")

        input("1")
        assertInputField(expected = "2+1")

        calculate()
        assertInputField(expected = "2+1")
        assertResult(expected = "3")
    }

    @Test
    fun sum_of_two_numbers_more_complex() = with(viewModel) {
        input("2")
        assertInputField(expected = "2")

        input("1")
        assertInputField(expected = "21")

        inputZero()
        assertInputField(expected = "210")

        inputZero()
        assertInputField(expected = "2100")

        plus()
        assertInputField(expected = "2100+")

        input("1")
        assertInputField(expected = "2100+1")

        inputZero()
        assertInputField(expected = "2100+10")

        input("2")
        assertInputField(expected = "2100+102")

        calculate()
        assertInputField(expected = "2100+102")
        assertResult(expected = "2202")
    }

    @Test
    fun sum_of_two_numbers_corner_case() = with(viewModel) {
        input("1")
        assertInputField(expected = "1")

        var expected = "1"
        repeat(9) {
            inputZero()
            expected += "0"
            assertInputField(expected = expected)
        }

        plus()
        assertInputField(expected = "1000000000+")

        input("2")
        assertInputField(expected = "1000000000+2")

        expected = "1000000000+2"
        repeat(9) {
            inputZero()
            expected += "0"
            assertInputField(expected = expected)
        }

        calculate()
        assertInputField(expected = "1000000000+2000000000")
        assertResult(expected = "3000000000")
    }

    @Test
    fun prevent_multiple_zeros() = with(viewModel) {
        repeat(10) {
            inputZero()
            assertInputField(expected = "0")
        }
        plus()
        assertInputField(expected = "0+")
        repeat(10) {
            inputZero()
            assertInputField(expected = "0+0")
        }
        calculate()
        assertInputField(expected = "0+0")
        assertResult(expected = "0")
    }

    @Test
    fun prevent_multiple_zeros_minus_operation() = with(viewModel) {
        repeat(10) {
            inputZero()
            assertInputField(expected = "0")
        }
        minus()
        assertInputField(expected = "0-")
        repeat(10) {
            inputZero()
            assertInputField(expected = "0-0")
        }
        calculate()
        assertInputField(expected = "0-0")
        assertResult(expected = "0")
    }

    @Test
    fun prevent_leading_zeros() = with(viewModel) {
        inputZero()
        assertInputField(expected = "0")

        input("1")
        assertInputField(expected = "1")

        plus()
        assertInputField(expected = "1+")

        inputZero()
        assertInputField(expected = "1+0")

        input("2")
        assertInputField(expected = "1+2")

        calculate()
        assertInputField(expected = "1+2")
        assertResult(expected = "3")
    }

    @Test
    fun prevent_minus_zero() = with(viewModel) {
        minus()
        assertInputField(expected = "-")

        inputZero()
        assertInputField(expected = "-")

        input("1")
        assertInputField(expected = "-1")

        plus()
        assertInputField(expected = "-1+")

        input("2")
        assertInputField(expected = "-1+2")

        inputZero()
        assertInputField(expected = "-1+20")

        calculate()
        assertInputField(expected = "-1+20")
        assertResult("19")
    }

    @Test
    fun prevent_minus_dot() = with(viewModel) {
        minus()
        assertInputField("-")
        inputDot()
        assertInputField("-")
        input("1")
        assertInputField("-1")
        multiply()
        assertInputField("-1*")
        input("2")
        assertInputField("-1*2")
        calculate()
        assertInputField("-1*2")
        assertResult("-2")
    }

    @Test
    fun prevent_multiple_plus_operations() = with(viewModel) {
        input("2")
        assertInputField(expected = "2")

        repeat(5) {
            plus()
            assertInputField(expected = "2+")
        }

        input("1")
        assertInputField(expected = "2+1")

        calculate()
        assertInputField(expected = "2+1")
        assertResult(expected = "3")
    }

    @Test
    fun prevent_multiple_minus_operations() = with(viewModel) {
        input("2")
        assertInputField(expected = "2")

        repeat(5) {
            minus()
            assertInputField(expected = "2-")
        }

        input("1")
        assertInputField(expected = "2-1")

        calculate()
        assertInputField(expected = "2-1")
        assertResult(expected = "1")
    }

    @Test
    fun prevent_leading_pluses() = with(viewModel) {
        plus()
        assertInputField(expected = "")

        input("1")
        assertInputField(expected = "1")

        plus()
        assertInputField(expected = "1+")

        input("2")
        assertInputField(expected = "1+2")

        calculate()
        assertInputField(expected = "1+2")
        assertResult(expected = "3")
    }

    @Test
    fun sum_of_more_than_two_numbers() = with(viewModel) {
        input("1")
        assertInputField("1")

        plus()
        assertInputField("1+")

        input("2")
        assertInputField("1+2")

        plus()
        assertInputField("3+")

        input("1")
        assertInputField("3+1")

        inputZero()
        assertInputField("3+10")

        plus()
        assertInputField("13+")

        input("2")
        assertInputField("13+2")

        calculate()
        assertInputField("13+2")
        assertResult("15")
    }

    @Test
    fun multiply_more_than_two_numbers() = with(viewModel) {
        input("2")
        assertInputField("2")

        multiply()
        assertInputField("2*")

        input("3")
        assertInputField("2*3")

        multiply()
        assertInputField("6*")

        input("2")
        assertInputField("6*2")

        inputZero()
        assertInputField("6*20")

        multiply()
        assertInputField("120*")

        input("2")
        assertInputField("120*2")

        calculate()
        assertInputField("120*2")
        assertResult("240")
    }

    @Test
    fun divide_more_than_two_numbers() = with(viewModel) {
        input("2")
        assertInputField("2")

        divide()
        assertInputField("2/")

        input("1")
        inputZero()
        assertInputField("2/10")

        divide()
        assertInputField("0.2/")

        input("2")
        assertInputField("0.2/2")

        inputZero()
        assertInputField("0.2/20")

        divide()
        assertInputField("0.01/")

        input("2")
        assertInputField("0.01/2")

        calculate()
        assertInputField("0.01/2")
        assertResult("0.005")
    }

    @Test
    fun divide_gives_infinity_after_result() = with(viewModel) {
        input("1")
        divide()
        inputZero()
        assertInputField("1/0")
        inputDot()
        assertInputField("1/0.")
        inputZero()
        assertInputField("1/0.0")
        divide()
        assertInputField("1/0.0")
        assertResult("infinity")
    }

    @Test
    fun divide_gives_uncertainty_after_result() = with(viewModel) {
        inputZero()
        assertInputField("0")
        inputDot()
        assertInputField("0.")
        inputZero()
        assertInputField("0.0")
        divide()
        inputZero()
        assertInputField("0.0/0")
        inputDot()
        assertInputField("0.0/0.")
        inputZero()
        assertInputField("0.0/0.0")
        divide()
        assertInputField("0.0/0.0")
        assertResult("uncertainty")
    }

    @Test
    fun diff_of_more_than_two_numbers() = with(viewModel) {
        input("1")
        assertInputField("1")

        minus()
        assertInputField("1-")

        input("2")
        assertInputField("1-2")

        minus()
        assertInputField("-1-")

        input("2")
        assertInputField("-1-2")

        inputZero()
        assertInputField("-1-20")

        minus()
        assertInputField("-21-")

        input("2")
        assertInputField("-21-2")

        calculate()
        assertInputField("-21-2")
        assertResult("-23")
    }

    @Test
    fun sum_after_equals() = with(viewModel) {
        input("1")
        assertInputField("1")

        plus()
        assertInputField("1+")

        input("2")
        assertInputField("1+2")

        calculate()
        assertInputField("1+2")
        assertResult("3")

        plus()
        assertInputField("3+")
        assertResult("")

        input("1")
        assertInputField("3+1")
        assertResult("")

        calculate()
        assertInputField("3+1")
        assertResult("4")

        plus()
        assertInputField("4+")
        assertResult("")

        input("2")
        assertInputField("4+2")
        assertResult("")

        plus()
        assertInputField("6+")
        assertResult("")

        input("1")
        assertInputField("6+1")
        assertResult("")

        calculate()
        assertInputField("6+1")
        assertResult("7")
    }

    @Test
    fun diff_after_equals() = with(viewModel) {
        input("1")
        assertInputField("1")

        minus()
        assertInputField("1-")

        input("2")
        assertInputField("1-2")

        calculate()
        assertInputField("1-2")
        assertResult("-1")

        minus()
        assertInputField("-1-")
        assertResult("")

        input("1")
        assertInputField("-1-1")
        assertResult("")

        calculate()
        assertInputField("-1-1")
        assertResult("-2")

        minus()
        assertInputField("-2-")
        assertResult("")

        input("2")
        assertInputField("-2-2")
        assertResult("")

        minus()
        assertInputField("-4-")
        assertResult("")

        input("1")
        assertInputField("-4-1")
        assertResult("")

        calculate()
        assertInputField("-4-1")
        assertResult("-5")
    }

    @Test
    fun prevent_equals_not_at_the_end() = with(viewModel) {
        repeat(3) {
            calculate()
            assertInputField(expected = "")
            assertResult("")
        }

        input("2")
        assertInputField(expected = "2")
        assertResult("")

        repeat(3) {
            calculate()
            assertInputField(expected = "2")
            assertResult("")
        }

        plus()
        assertInputField(expected = "2+")
        assertResult("")

        repeat(3) {
            calculate()
            assertInputField(expected = "2+")
            assertResult("")
        }

        input("1")
        assertInputField(expected = "2+1")
        assertResult("")

        repeat(3) {
            calculate()
            assertInputField(expected = "2+1")
            assertResult("3")
        }
    }

    @Test
    fun not_number_after_dot() = with(viewModel) {
        input("1")
        assertInputField("1")
        inputDot()
        assertInputField("1.")
        plus()
        assertInputField("1.")
        calculate()
        assertInputField("1.")
        assertResult("")

        inputZero()
        assertInputField("1.0")
        divide()
        assertInputField("1.0/")
        inputZero()
        assertInputField("1.0/0")
        inputDot()
        assertInputField("1.0/0.")
        calculate()
        assertInputField("1.0/0.")
        plus()
        assertInputField("1.0/0.")
        input("1")
        assertInputField("1.0/0.1")
        calculate()
        assertInputField("1.0/0.1")
        assertResult("10")
    }

    @Test
    fun prevent_equals_after_minus() = with(viewModel) {
        repeat(3) {
            calculate()
            assertInputField(expected = "")
            assertResult("")
        }

        input("2")
        assertInputField(expected = "2")
        assertResult("")

        repeat(3) {
            calculate()
            assertInputField(expected = "2")
            assertResult("")
        }

        minus()
        assertInputField(expected = "2-")
        assertResult("")

        repeat(3) {
            calculate()
            assertInputField(expected = "2-")
            assertResult("")
        }

        input("1")
        assertInputField(expected = "2-1")
        assertResult("")

        repeat(3) {
            calculate()
            assertInputField(expected = "2-1")
            assertResult("1")
        }
    }

    @Test
    fun diff_of_two_numbers() = with(viewModel) {
        input("1")
        assertInputField(expected = "1")

        minus()
        assertInputField(expected = "1-")

        input("2")
        assertInputField(expected = "1-2")

        calculate()
        assertInputField(expected = "1-2")
        assertResult(expected = "-1")
    }

    @Test
    fun diff_sign_ahead() = with(viewModel) {
        repeat(3) {
            minus()
            assertInputField("-")
        }

        input("1")
        assertInputField("-1")

        minus()
        assertInputField("-1-")

        input("2")
        assertInputField("-1-2")

        calculate()
        assertInputField("-1-2")
        assertResult("-3")
    }

    @Test
    fun change_minus_to_plus() = with(viewModel) {
        minus()
        assertInputField("-")

        plus()
        assertInputField("")

        input("2")
        assertInputField("2")

        minus()
        assertInputField("2-")

        plus()
        assertInputField("2+")

        input("1")
        assertInputField("2+1")

        minus()
        assertInputField("3-")
        assertResult("")

        plus()
        assertInputField("3+")
        assertResult("")

        inputZero()
        assertInputField("3+0")
        assertResult("")

        calculate()
        assertInputField("3+0")
        assertResult("3")
    }

    @Test
    fun multiply_zeros() = with(viewModel) {
        repeat(3) {
            multiply()
            assertInputField("")
        }
        repeat(3) {
            inputZero()
            assertInputField("0")
        }
        repeat(3) {
            multiply()
            assertInputField("0*")
        }
        repeat(3) {
            inputZero()
            assertInputField("0*0")
        }
        repeat(3) {
            calculate()
            assertInputField("0*0")
            assertResult("0")
        }
    }

    @Test
    fun multiply_several_times() = with(viewModel) {
        input("1")
        assertInputField("1")

        input("1")
        assertInputField("11")

        multiply()
        assertInputField("11*")

        input("2")
        assertInputField("11*2")

        calculate()
        assertInputField("11*2")
        assertResult("22")

        multiply()
        assertInputField("22*")
        assertResult("")

        input("1")
        assertInputField("22*1")

        inputZero()
        assertInputField("22*10")

        calculate()
        assertInputField("22*10")
        assertResult("220")
    }

    @Test
    fun multiply_several_times_changed_operation() = with(viewModel) {
        input("1")
        assertInputField("1")

        input("1")
        assertInputField("11")

        multiply()
        assertInputField("11*")

        input("2")
        assertInputField("11*2")

        plus()
        assertInputField("22+")
        assertResult("")

        multiply()
        assertInputField("22*")
        assertResult("")

        input("1")
        assertInputField("22*1")

        inputZero()
        assertInputField("22*10")

        calculate()
        assertInputField("22*10")
        assertResult("220")
    }

    @Test
    fun divide_number_by_zero() = with(viewModel) {
        repeat(3) {
            divide()
            assertInputField("")
        }

        input("1")
        assertInputField("1")

        repeat(3) {
            divide()
            assertInputField("1/")
        }

        repeat(3) {
            inputZero()
            assertInputField("1/0")
        }

        calculate()
        assertInputField("1/0")
        assertResult("infinity")
    }

    @Test
    fun divide_number_by_zero_decimal() = with(viewModel) {
        input("1")
        assertInputField("1")

        divide()
        assertInputField("1/")

        inputZero()
        assertInputField("1/0")

        inputDot()
        assertInputField("1/0.")

        inputZero()
        assertInputField("1/0.0")

        calculate()
        assertInputField("1/0.0")
        assertResult("infinity")
    }

    @Test
    fun divide_by_zero_and_then_operation() = with(viewModel) {
        divide_number_by_zero()

        plus()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")
        assertResult("")
    }

    @Test
    fun divide_by_zero_and_then_number() = with(viewModel) {
        divide_number_by_zero()

        input("1")
        assertInputField("1")
        assertResult("")
    }

    @Test
    fun divide_decimal() = with(viewModel) {
        input("1")
        assertInputField("1")
        inputZero()
        assertInputField("10")
        input("2")
        assertInputField("102")

        divide()
        assertInputField("102/")

        input("1")
        assertInputField("102/1")

        input("2")
        assertInputField("102/12")

        calculate()
        assertInputField("102/12")
        assertResult("8.5")
    }

    @Test
    fun sum_of_decimals() = with(viewModel) {
        divide_decimal()

        plus()
        assertInputField("8.5+")
        input("1")
        assertInputField("8.5+1")

        calculate()
        assertInputField("8.5+1")
        assertResult("9.5")
    }

    @Test
    fun uncertainty() = with(viewModel) {
        inputZero()
        assertInputField("0")

        divide()
        assertInputField("0/")

        inputZero()
        assertInputField("0/0")

        calculate()
        assertInputField("0/0")
        assertResult("uncertainty")
    }

    @Test
    fun uncertainty_and_then_operation() = with(viewModel) {
        uncertainty()

        plus()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")
        assertResult("")
    }

    @Test
    fun uncertainty_and_then_number() = with(viewModel) {
        uncertainty()

        input("1")
        assertInputField("1")
        assertResult("")
    }

    @Test
    fun clear_all() = with(viewModel) {
        clearAll()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")

        clearAll()
        assertInputField("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")

        clearAll()
        assertInputField("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")
        input("2")
        assertInputField("1+2")

        clearAll()
        assertInputField("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")
        input("2")
        assertInputField("1+2")
        calculate()
        assertInputField("1+2")
        assertResult("3")

        clearAll()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")
        input("2")
        assertInputField("1+2")
        calculate()
        assertInputField("1+2")
        assertResult("3")
    }

    @Test
    fun backspace() = with(viewModel) {
        this.backspace()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")

        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")

        this.backspace()
        assertInputField("1")
        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")
        input("2")
        assertInputField("1+2")

        this.backspace()
        assertInputField("1+")
        this.backspace()
        assertInputField("1")
        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        plus()
        assertInputField("1+")
        input("2")
        assertInputField("1+2")
        calculate()
        assertInputField("1+2")
        assertResult("3")

        this.backspace()
        assertInputField("")
        assertResult("")
    }

    @Test
    fun backspace_complex() = with(viewModel) {
        this.backspace()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")

        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        input("2")
        assertInputField("12")
        this.backspace()
        assertInputField("1")
        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        input("2")
        assertInputField("12")
        plus()
        assertInputField("12+")
        this.backspace()
        assertInputField("12")
        this.backspace()
        assertInputField("1")
        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        input("2")
        assertInputField("12")
        plus()
        assertInputField("12+")
        input("2")
        assertInputField("12+2")
        this.backspace()
        assertInputField("12+")
        this.backspace()
        assertInputField("12")
        this.backspace()
        assertInputField("1")
        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        input("2")
        assertInputField("12")
        plus()
        assertInputField("12+")
        input("2")
        assertInputField("12+2")
        inputZero()
        assertInputField("12+20")
        this.backspace()
        assertInputField("12+2")
        this.backspace()
        assertInputField("12+")
        this.backspace()
        assertInputField("12")
        this.backspace()
        assertInputField("1")
        this.backspace()
        assertInputField("")

        input("1")
        assertInputField("1")
        input("2")
        assertInputField("12")
        plus()
        assertInputField("12+")
        input("2")
        assertInputField("12+2")
        inputZero()
        assertInputField("12+20")
        calculate()
        assertInputField("12+20")
        assertResult("32")

        this.backspace()
        assertInputField("3")
        assertResult("")

        multiply()
        assertInputField("3*")
        input("2")
        assertInputField("3*2")
        calculate()
        assertInputField("3*2")
        assertResult("6")
    }

    @Test
    fun dot() = with(viewModel) {
        repeat(3) {
            inputDot()
            assertInputField("")
        }
        input("1")
        assertInputField("1")
        input("2")
        assertInputField("12")
        repeat(3) {
            inputDot()
            assertInputField("12.")
        }
        inputZero()
        assertInputField("12.0")
        repeat(3) {
            inputDot()
            assertInputField("12.0")
        }
        input("1")
        assertInputField("12.01")

        plus()
        assertInputField("12.01+")

        repeat(3) {
            inputDot()
            assertInputField("12.01+")
        }
        input("2")
        assertInputField("12.01+2")
        inputZero()
        assertInputField("12.01+20")

        repeat(3) {
            inputDot()
            assertInputField("12.01+20.")
        }

        input("1")
        assertInputField("12.01+20.1")

        repeat(3) {
            inputDot()
            assertInputField("12.01+20.1")
        }
        input("2")
        assertInputField("12.01+20.12")

        calculate()
        assertInputField("12.01+20.12")
        assertResult("32.13")

        repeat(3) {
            inputDot()
            assertInputField("12.01+20.12")
            assertResult("32.13")
        }

        clearAll()
        assertInputField("")
        assertResult("")

        input("1")
        assertInputField("1")

        plus()
        assertInputField("1+")

        input("2")
        assertInputField("1+2")

        calculate()
        assertInputField("1+2")
        assertResult("3")

        inputDot()
        assertInputField("3.")
        assertResult("")

        this.backspace()
        assertInputField("3")

        multiply()
        assertInputField("3*")
        input("2")
        assertInputField("3*2")

        inputDot()
        assertInputField("3*2.")

        input("1")
        assertInputField("3*2.1")

        calculate()
        assertInputField("3*2.1")
        assertResult("6.3")
    }

    @Test
    fun final() = with(viewModel){
        input("1")
        assertInputField("1")
        inputDot()
        assertInputField("1.")
        minus()
        assertInputField("1.")

        multiply()
        assertInputField("1.")

        divide()
        assertInputField("1.")

        backspace()
        assertInputField("1")

        plus()
        assertInputField("1+")

        repeat(2) {
            minus()
            assertInputField("1-")
        }

        repeat(2) {
            divide()
            assertInputField("1/")
        }

        input("2")
        assertInputField("1/2")

        inputDot()
        assertInputField("1/2.")

        minus()
        assertInputField("1/2.")

        multiply()
        assertInputField("1/2.")

        divide()
        assertInputField("1/2.")
    }

    fun assertDivideCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, divideCalledCount)
    private fun assertInputField(expected: String) {
        assertEquals(expected, inputFlow.value)
    }

    private fun assertResult(expected: String) {
        assertEquals(expected, resultFlow.value)
    }
}
