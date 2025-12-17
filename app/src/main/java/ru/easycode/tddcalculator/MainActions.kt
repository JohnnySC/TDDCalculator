package ru.easycode.tddcalculator

interface MainActions {

    fun input(number: String)
    fun inputZero()
    fun inputDot()

    fun plus()
    fun minus()
    fun multiply()
    fun divide()
    fun calculate()
    fun backspace()
    fun clearAll()
}