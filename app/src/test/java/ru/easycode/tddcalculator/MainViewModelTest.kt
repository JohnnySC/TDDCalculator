package ru.easycode.tddcalculator

import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun sum_of_two_numbers() {
        val inputFlow: StateFlow<String> = viewModel.inputFlow
        val resultFlow: StateFlow<String> = viewModel.resultFlow

        assertEquals("", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.inputOne()
        assertEquals("1", inputFlow.value)

        viewModel.plus()
        assertEquals("1+", inputFlow.value)

        viewModel.inputTwo()
        assertEquals("1+2", inputFlow.value)

        viewModel.calculate()
        assertEquals("1+2", inputFlow.value)
        assertEquals("3", resultFlow.value)
    }
}