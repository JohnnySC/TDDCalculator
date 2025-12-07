package ru.easycode.tddcalculator

data class CalculationParts(
    val left: String = "",
    val operation: String = "",
    val right: String = "",
) {
    override fun toString(): String {
        return "$left$operation$right"
    }
}