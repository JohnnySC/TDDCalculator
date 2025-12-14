package ru.easycode.tddcalculator

data class CalculationParts(
    val left: String = "",
    val operation: String = "",
    val right: String = "",
) {
    override fun toString(): String {
        return "$left$operation$right"
    }

    fun calculate(repository: MainRepository): String {
        return when (operation) {
            "+" -> repository.sum(left, right)
            "-" -> repository.diff(left, right)
            "*" -> repository.multiply(left, right)
            else -> throw IllegalStateException("unknown operation $operation")
        }
    }
}