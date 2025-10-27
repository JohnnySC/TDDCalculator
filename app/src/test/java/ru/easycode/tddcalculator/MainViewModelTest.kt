package ru.easycode.tddcalculator

import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var inputFlow: StateFlow<String>
    private lateinit var resultFlow: StateFlow<String>

    @Before
    fun setup() {
        viewModel = MainViewModel()
        inputFlow = viewModel.inputFlow
        resultFlow = viewModel.resultFlow
        assertEquals("", inputFlow.value)
        assertEquals("", resultFlow.value)
    }

    @Test
    fun sum_of_two_numbers() {
        viewModel.inputTwo()
        assertEquals("2", inputFlow.value)

        viewModel.plus()
        assertEquals("2+", inputFlow.value)

        viewModel.inputOne()
        assertEquals("2+1", inputFlow.value)

        viewModel.calculate()
        assertEquals("2+1", inputFlow.value)
        assertEquals("3", resultFlow.value)
    }

    @Test
    fun sum_of_two_numbers_more_complex() {
        viewModel.inputTwo()
        assertEquals("2", inputFlow.value)

        viewModel.inputOne()
        assertEquals("21", inputFlow.value)

        viewModel.inputZero()
        assertEquals("210", inputFlow.value)

        viewModel.inputZero()
        assertEquals("2100", inputFlow.value)

        viewModel.plus()
        assertEquals("2100+", inputFlow.value)

        viewModel.inputOne()
        assertEquals("2100+1", inputFlow.value)

        viewModel.inputZero()
        assertEquals("2100+10", inputFlow.value)

        viewModel.inputTwo()
        assertEquals("2100+102", inputFlow.value)

        viewModel.calculate()
        assertEquals("2100+102", inputFlow.value)
        assertEquals("2202", resultFlow.value)
    }


    @Test
    fun sum_of_two_numbers_corner_case() {
        viewModel.inputOne()
        assertEquals("1", inputFlow.value)

        var expected = "1"
        repeat(9) {
            viewModel.inputZero()
            expected += "0"
            assertEquals(expected, inputFlow.value)
        }

        viewModel.plus()
        assertEquals("1000000000+", inputFlow.value)

        viewModel.inputTwo()
        assertEquals("1000000000+2", inputFlow.value)

        expected = "1000000000+2"
        repeat(9) {
            viewModel.inputZero()
            expected += "0"
            assertEquals(expected, inputFlow.value)
        }

        viewModel.calculate()
        assertEquals("1000000000+2000000000", inputFlow.value)
        assertEquals("3000000000", resultFlow.value)
    }

    @Test
    fun prevent_multiple_zeros() {
        repeat(10) {
            viewModel.inputZero()
            assertEquals("0", inputFlow.value)
        }
        viewModel.plus()
        assertEquals("0+", inputFlow.value)
        repeat(10) {
            viewModel.inputZero()
            assertEquals("0+0", inputFlow.value)
        }
        viewModel.calculate()
        assertEquals("0+0", inputFlow.value)
        assertEquals("0", resultFlow.value)
    }

    @Test
    fun prevent_leading_zeros() {
        viewModel.inputZero()
        assertEquals("0", inputFlow.value)

        viewModel.inputOne()
        assertEquals("1", inputFlow.value)

        viewModel.plus()
        assertEquals("1+", inputFlow.value)

        viewModel.inputZero()
        assertEquals("1+0", inputFlow.value)

        viewModel.inputTwo()
        assertEquals("1+2", inputFlow.value)

        viewModel.calculate()
        assertEquals("1+2", inputFlow.value)
        assertEquals("3", resultFlow.value)
    }
}