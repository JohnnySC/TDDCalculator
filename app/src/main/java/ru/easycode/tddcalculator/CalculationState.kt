package ru.easycode.tddcalculator

import java.math.BigDecimal

interface CalculationState {

    fun inputNumber(
        number: String, calculationParts: CalculationParts, updateCallback: UpdateCallback
    )

    fun inputZero(
        calculationParts: CalculationParts, updateCallback: UpdateCallback
    )

    fun inputDot(
        calculationParts: CalculationParts, updateCallback: UpdateCallback
    )

    fun calculate(
        repository: MainRepository,
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    ) = Unit

    fun backspace(
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    )

    fun plus(
        repository: MainRepository,
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    )

    fun minus(
        repository: MainRepository,
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    )

    fun multiply(
        repository: MainRepository,
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    )

    fun divide(
        repository: MainRepository,
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    )

    fun clear(updateCallback: UpdateCallback) = with(updateCallback) {
        updateState(DefiningLeft())
        updateCalculationParts(CalculationParts())
        updateInput()
        updateResult("")
    }

    class DefiningLeft : CalculationState {

        override fun inputNumber(
            number: String, calculationParts: CalculationParts, updateCallback: UpdateCallback
        ) {
            if (calculationParts.left == "0") {
                updateCallback.updateCalculationParts(CalculationParts(left = number))
            } else {
                val newCalculationParts = CalculationParts(left = calculationParts.left + number)
                updateCallback.updateCalculationParts(newCalculationParts)
            }
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun inputZero(
            calculationParts: CalculationParts, updateCallback: UpdateCallback
        ) {
            if (calculationParts.left == "0") return
            if (calculationParts.left == "-") return
            val newCalculationParts = CalculationParts(left = calculationParts.left + "0")
            updateCallback.updateCalculationParts(newCalculationParts)
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun inputDot(
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.left.isEmpty()) return
            if (calculationParts.left == "-") return
            if (calculationParts.left.contains(".")) return
            updateCallback.updateCalculationParts(CalculationParts(left = calculationParts.left + "."))
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun backspace(
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            updateCallback.updateCalculationParts(
                CalculationParts(left = calculationParts.left.dropLast(1))
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun plus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.left.endsWith("."))
                return
            if (calculationParts.left.isEmpty()) {
                updateCallback.updateCalculationParts(CalculationParts())
                updateCallback.updateInput()
                updateCallback.updateResult("")
                return
            }
            if (calculationParts.left == "-") {
                updateCallback.updateCalculationParts(CalculationParts())
            } else {
                updateCallback.updateCalculationParts(
                    CalculationParts(
                        left = calculationParts.left, operation = "+"
                    )
                )
                updateCallback.updateState(DefineOperation())
            }
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun minus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.left.endsWith("."))
                return
            if (calculationParts.left == "-") return
            if (calculationParts.left.isEmpty()) {
                updateCallback.updateCalculationParts(CalculationParts(left = "-"))
            } else {
                updateCallback.updateCalculationParts(
                    CalculationParts(
                        left = calculationParts.left, operation = "-"
                    )
                )
                updateCallback.updateState(DefineOperation())
            }
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun multiply(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.left.endsWith("."))
                return
            if (calculationParts.left.isEmpty()) {
                updateCallback.updateCalculationParts(CalculationParts())
                updateCallback.updateInput()
                updateCallback.updateResult("")
                return
            }
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left, operation = "*"
                )
            )
            updateCallback.updateState(DefineOperation())
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun divide(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.left.endsWith("."))
                return
            if (calculationParts.left.isEmpty()) {
                updateCallback.updateCalculationParts(CalculationParts())
                updateCallback.updateInput()
                updateCallback.updateResult("")
                return
            }
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left, operation = "/"
                )
            )
            updateCallback.updateState(DefineOperation())
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }
    }

    class DefineOperation : CalculationState {

        override fun inputNumber(
            number: String, calculationParts: CalculationParts, updateCallback: UpdateCallback
        ) {
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left,
                    operation = calculationParts.operation,
                    right = number
                )
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
            updateCallback.updateState(DefiningRight())
        }

        override fun inputZero(
            calculationParts: CalculationParts, updateCallback: UpdateCallback
        ) = inputNumber("0", calculationParts, updateCallback)

        override fun inputDot(
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) = Unit

        override fun backspace(
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            updateCallback.updateCalculationParts(CalculationParts(left = calculationParts.left))
            updateCallback.updateInput()
            updateCallback.updateState(DefiningLeft())
        }

        override fun plus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.operation == "+") return
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left, operation = "+"
                )
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun minus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.operation == "-") return
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left, operation = "-"
                )
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun multiply(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.operation == "*") return
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left, operation = "*"
                )
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun divide(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.operation == "/") return
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left, operation = "/"
                )
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }
    }

    class DefiningRight : CalculationState {

        override fun inputNumber(
            number: String, calculationParts: CalculationParts, updateCallback: UpdateCallback
        ) {
            if (calculationParts.right == "0") {
                updateCallback.updateCalculationParts(
                    CalculationParts(
                        left = calculationParts.left,
                        operation = calculationParts.operation,
                        right = number
                    )
                )
            } else {
                val newCalculationParts = CalculationParts(
                    left = calculationParts.left,
                    operation = calculationParts.operation,
                    right = calculationParts.right + number
                )
                updateCallback.updateCalculationParts(newCalculationParts)
            }
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun inputZero(
            calculationParts: CalculationParts, updateCallback: UpdateCallback
        ) {
            if (calculationParts.right == "0") return
            val newCalculationParts = CalculationParts(
                left = calculationParts.left,
                operation = calculationParts.operation,
                right = calculationParts.right + "0"
            )
            updateCallback.updateCalculationParts(newCalculationParts)
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun inputDot(
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.contains(".")) return
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = calculationParts.left,
                    operation = calculationParts.operation,
                    right = calculationParts.right + "."
                )
            )
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun calculate(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.endsWith("."))
                return
            if (BigDecimal(calculationParts.right).compareTo(BigDecimal.ZERO) == 0 && calculationParts.operation == "/") {
                val result = if (BigDecimal(calculationParts.left).compareTo(BigDecimal.ZERO) == 0) "uncertainty" else "infinity"
                updateCallback.updateResult(result)
                updateCallback.updateCalculationParts(CalculationParts())
                updateCallback.updateState(DefiningLeft())
            } else {
                val result = calculationParts.calculate(repository)
                updateCallback.updateResult(result)
                updateCallback.updateState(DefiningLeft())
                updateCallback.updateCalculationParts(CalculationParts(left = result))
            }
        }

        override fun backspace(
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.length == 1) {
                updateCallback.updateState(DefineOperation())
                updateCallback.updateCalculationParts(
                    CalculationParts(
                        left = calculationParts.left,
                        operation = calculationParts.operation
                    )
                )
                updateCallback.updateInput()
            } else {
                updateCallback.updateCalculationParts(
                    CalculationParts(
                        left = calculationParts.left,
                        operation = calculationParts.operation,
                        right = calculationParts.right.dropLast(1)
                    )
                )
                updateCallback.updateInput()
            }
        }

        override fun plus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.endsWith("."))
                return
            val result = calculationParts.calculate(repository)
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = result, operation = "+"
                )
            )
            updateCallback.updateState(DefineOperation())
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun minus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.endsWith("."))
                return
            val result = calculationParts.calculate(repository)
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = result, operation = "-"
                )
            )
            updateCallback.updateState(DefineOperation())
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun multiply(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.endsWith("."))
                return
            val result = calculationParts.calculate(repository)
            updateCallback.updateCalculationParts(
                CalculationParts(
                    left = result, operation = "*"
                )
            )
            updateCallback.updateState(DefineOperation())
            updateCallback.updateInput()
            updateCallback.updateResult("")
        }

        override fun divide(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right.endsWith("."))
                return
            if (BigDecimal(calculationParts.right).compareTo(BigDecimal.ZERO) == 0) {
                val result = if (BigDecimal(calculationParts.left).compareTo(BigDecimal.ZERO) == 0) "uncertainty" else "infinity"
                updateCallback.updateResult(result)
                updateCallback.updateCalculationParts(CalculationParts())
                updateCallback.updateState(DefiningLeft())
            } else {
                val result = calculationParts.calculate(repository)
                updateCallback.updateCalculationParts(
                    CalculationParts(
                        left = result, operation = "/"
                    )
                )
                updateCallback.updateState(DefineOperation())
                updateCallback.updateInput()
                updateCallback.updateResult("")
            }
        }
    }
}