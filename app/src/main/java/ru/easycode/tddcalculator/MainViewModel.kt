package ru.easycode.tddcalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel(), MainActions {

    private val inputMutableFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val resultMutableFlow: MutableStateFlow<String> = MutableStateFlow("")
    val inputFlow: StateFlow<String>
        get() = inputMutableFlow
    val resultFlow: StateFlow<String>
        get() = resultMutableFlow

    private var left: Int = 0
    private var right: Int = 0

    override fun inputOne() {
        left = 1
        inputMutableFlow.value = left.toString()
    }

    override fun inputTwo() {
        right = 2
        val before = inputFlow.value
        inputMutableFlow.value = before + right
    }

    override fun inputZero() {
        //todo next step
    }

    override fun plus() {
        val before = inputFlow.value
        val result = "$before+"
        inputMutableFlow.value = result
    }

    override fun calculate() {
        val result = left + right
        resultMutableFlow.value = result.toString()
    }
}