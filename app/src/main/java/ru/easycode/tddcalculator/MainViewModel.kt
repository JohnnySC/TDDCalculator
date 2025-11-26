package ru.easycode.tddcalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val repository: MainRepository = MainRepository.Base()
) : ViewModel(), MainActions {

    private val inputMutableFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val resultMutableFlow: MutableStateFlow<String> = MutableStateFlow("")
    val inputFlow: StateFlow<String>
        get() = inputMutableFlow
    val resultFlow: StateFlow<String>
        get() = resultMutableFlow

    private var addToLeft = true
    private var left: String = ""
    private var right: String = ""
    private var operation: String = ""

    override fun inputOne() {
        if (addToLeft) {
            if (left == "0") {
                left = "1"
            } else {
                left += "1"
            }
            inputMutableFlow.value = left
        } else {
            if (right == "0") {
                right = "1"
            } else {
                right += "1"
            }
            inputMutableFlow.value = "$left$operation$right"
        }
    }

    override fun inputTwo() {
        if (addToLeft) {
            if (left == "0") {
                left = "2"
            } else {
                left += "2"
            }
            inputMutableFlow.value = left
        } else {
            if (right == "0") {
                right = "2"
            } else {
                right += "2"
            }
            inputMutableFlow.value = "$left$operation$right"
        }
    }

    override fun inputZero() {
        if (addToLeft) {
            if (left != "0") {
                left += "0"
                inputMutableFlow.value = left
            }
        } else {
            if (right != "0") {
                right += "0"
                inputMutableFlow.value = "$left+$right"
            }
        }
    }

    override fun plus() {
        operation = "+"
        if (resultFlow.value.isNotEmpty()) {
            left = resultFlow.value
            right = ""
            inputMutableFlow.value = "$left+"
            resultMutableFlow.value = ""
        } else if (left.isNotEmpty() && right.isNotEmpty()) {
            val result = repository.sum(left, right)
            left = result
            right = ""
            inputMutableFlow.value = "$left+"
        } else {
            val before = inputFlow.value
            if (!before.endsWith("+") && left.isNotEmpty()) {
                addToLeft = false
                val result = "$before+"
                inputMutableFlow.value = result
            }
        }
    }

    override fun minus() {
        operation = "-"
        if (resultFlow.value.isNotEmpty()) {
            left = resultFlow.value
            right = ""
            inputMutableFlow.value = "$left-"
            resultMutableFlow.value = ""
        } else if (left.isNotEmpty() && right.isNotEmpty()) {
            val result = repository.diff(left, right)
            left = result
            right = ""
            inputMutableFlow.value = "$left-"
        } else {
            val before = inputFlow.value
            if (before.isEmpty()) {
                left = "-"
                inputMutableFlow.value = left
            } else if (!before.endsWith("-") && left.isNotEmpty()) {
                addToLeft = false
                val result = "$before-"
                inputMutableFlow.value = result
            }
        }
    }

    override fun calculate() {
        if (left.isNotEmpty() && right.isNotEmpty() && resultFlow.value.isEmpty()) {
            val result = when (operation) {
                "+" -> repository.sum(left, right)
                "-" -> repository.diff(left, right)
                else -> ""
            }
            operation = ""
            resultMutableFlow.value = result
        }
    }
}