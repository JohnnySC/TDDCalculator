package ru.easycode.tddcalculator

import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var repository: FakeMainRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var inputFlow: StateFlow<String>
    private lateinit var resultFlow: StateFlow<String>

    @Before
    fun setup() {
        repository = FakeMainRepository()
        viewModel = MainViewModel(repository = repository)
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

    @Test
    fun prevent_multiple_plus_operations() {
        viewModel.inputTwo()
        assertEquals("2", inputFlow.value)

        repeat(5) {
            viewModel.plus()
            assertEquals("2+", inputFlow.value)
        }

        viewModel.inputOne()
        assertEquals("2+1", inputFlow.value)

        viewModel.calculate()
        assertEquals("2+1", inputFlow.value)
        assertEquals("3", resultFlow.value)
    }

    @Test
    fun prevent_leading_pluses() {
        viewModel.plus()
        assertEquals("", inputFlow.value)

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

    @Test
    fun sum_of_more_than_two_numbers() {
        viewModel.inputOne()
        assertEquals("1", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.plus()
        assertEquals("1+", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.inputTwo()
        assertEquals("1+2", inputFlow.value)
        assertEquals("", resultFlow.value)

        repeat(3) {
            viewModel.plus()
            repository.assertSumCalled(expectedTimes = 1)
            assertEquals("3+", inputFlow.value)
            assertEquals("", resultFlow.value)
        }

        viewModel.inputOne()
        assertEquals("3+1", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.inputZero()
        assertEquals("3+10", inputFlow.value)
        assertEquals("", resultFlow.value)

        repeat(3) {
            viewModel.plus()
            repository.assertSumCalled(expectedTimes = 2)
            assertEquals("13+", inputFlow.value)
            assertEquals("", resultFlow.value)
        }

        viewModel.inputTwo()
        assertEquals("13+2", inputFlow.value)
        assertEquals("", resultFlow.value)

        repeat(3) {
            viewModel.calculate()
            repository.assertSumCalled(expectedTimes = 3)
            assertEquals("13+2", inputFlow.value)
            assertEquals("15", resultFlow.value)
        }
    }

    @Test
    fun sum_after_equals() {
        viewModel.inputOne()
        assertEquals("1", inputFlow.value)

        viewModel.plus()
        assertEquals("1+", inputFlow.value)

        viewModel.inputTwo()
        assertEquals("1+2", inputFlow.value)

        viewModel.calculate()
        assertEquals("1+2", inputFlow.value)
        assertEquals("3", resultFlow.value)

        viewModel.plus()
        assertEquals("3+", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.inputOne()
        assertEquals("3+1", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.calculate()
        assertEquals("3+1", inputFlow.value)
        assertEquals("4", resultFlow.value)

        viewModel.plus()
        assertEquals("4+", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.inputTwo()
        assertEquals("4+2", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.plus()
        assertEquals("6+", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.inputOne()
        assertEquals("6+1", inputFlow.value)
        assertEquals("", resultFlow.value)

        viewModel.calculate()
        assertEquals("6+1", inputFlow.value)
        assertEquals("7", resultFlow.value)
    }

    @Test
    fun prevent_equals_not_at_the_end() {
        repeat(3) {
            viewModel.calculate()
            assertEquals("", inputFlow.value)
            assertEquals("", resultFlow.value)
        }

        viewModel.inputTwo()
        assertEquals("2", inputFlow.value)
        assertEquals("", resultFlow.value)

        repeat(3) {
            viewModel.calculate()
            assertEquals("2", inputFlow.value)
            assertEquals("", resultFlow.value)
        }

        viewModel.plus()
        assertEquals("2+", inputFlow.value)
        assertEquals("", resultFlow.value)

        repeat(3) {
            viewModel.calculate()
            assertEquals("2+", inputFlow.value)
            assertEquals("", resultFlow.value)
        }

        viewModel.inputOne()
        assertEquals("2+1", inputFlow.value)
        assertEquals("", resultFlow.value)

        repeat(3) {
            viewModel.calculate()
            assertEquals("2+1", inputFlow.value)
            assertEquals("3", resultFlow.value)
        }
        repository.assertSumCalled(expectedTimes = 1)
    }
}

private class FakeMainRepository(
    private val base: MainRepository = MainRepository.Base()
) : MainRepository {

    private var count = 0

    override fun sum(left: String, right: String): String {
        count++
        return base.sum(left, right)
    }

    fun assertSumCalled(expectedTimes: Int) {
        assertEquals(expectedTimes, count)
    }
}