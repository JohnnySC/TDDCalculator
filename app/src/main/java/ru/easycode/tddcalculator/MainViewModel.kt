package ru.easycode.tddcalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val repository: MainRepository = MainRepository.Base()
) : ViewModel(), MainActions, UpdateCallback {

    private var state: CalculationState = CalculationState.DefiningLeft()
    private var calculationParts = CalculationParts("", "", "")

    private val inputMutableFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val resultMutableFlow: MutableStateFlow<String> = MutableStateFlow("")
    val inputFlow: StateFlow<String>
        get() = inputMutableFlow
    val resultFlow: StateFlow<String>
        get() = resultMutableFlow

    override fun updateCalculationParts(calculationParts: CalculationParts) {
        this.calculationParts = calculationParts
    }

    override fun updateState(state: CalculationState) {
        this.state = state
    }

    override fun updateInput() {
        inputMutableFlow.value = calculationParts.toString()
    }

    override fun updateResult(result: String) {
        resultMutableFlow.value = result
    }

    override fun inputOne() {
        state.inputNumber("1", calculationParts, this)
    }

    override fun inputTwo() {
        state.inputNumber("2", calculationParts, this)
    }

    override fun inputZero() {
        state.inputZero(calculationParts, this)
    }

    override fun inputDot() {
        state.inputDot(calculationParts, this)
    }

    override fun plus() {
        state.plus(repository, calculationParts, this)
    }

    override fun minus() {
        state.minus(repository, calculationParts, this)
    }

    override fun multiply() {
        state.multiply(repository, calculationParts, this)
    }

    override fun divide() {
        state.divide(repository, calculationParts, this)
    }

    override fun calculate() {
        state.calculate(repository, calculationParts, this)
    }

    override fun backspace() {
        state.backspace(calculationParts, this)
    }

    override fun clearAll() {
        state.clear(this)
    }
}

interface UpdateCallback {

    fun updateCalculationParts(calculationParts: CalculationParts)

    fun updateState(state: CalculationState)

    fun updateInput()

    fun updateResult(result: String)
}