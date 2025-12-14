package ru.easycode.tddcalculator

interface CalculationState {

    fun inputNumber(
        number: String, calculationParts: CalculationParts, updateCallback: UpdateCallback
    )

    fun inputZero(
        calculationParts: CalculationParts, updateCallback: UpdateCallback
    )

    fun calculate(
        repository: MainRepository,
        calculationParts: CalculationParts,
        updateCallback: UpdateCallback
    ) = Unit

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

        override fun plus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
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

        override fun calculate(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
            if (calculationParts.right == "0") {
                updateCallback.updateResult("infinity")
                updateCallback.updateCalculationParts(CalculationParts())
                updateCallback.updateState(DefiningLeft())
            } else {
                val result = calculationParts.calculate(repository)
                updateCallback.updateResult(result)
                updateCallback.updateState(DefiningLeft())
                updateCallback.updateCalculationParts(CalculationParts(left = result))
            }
        }

        override fun plus(
            repository: MainRepository,
            calculationParts: CalculationParts,
            updateCallback: UpdateCallback
        ) {
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
            if (calculationParts.right == "0") {
                updateCallback.updateResult("infinity")
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