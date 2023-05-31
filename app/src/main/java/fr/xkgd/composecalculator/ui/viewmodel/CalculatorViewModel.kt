package fr.xkgd.composecalculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import fr.xkgd.composecalculator.ui.calculator.CalculatorAction
import fr.xkgd.composecalculator.ui.calculator.CalculatorOperation
import kotlinx.coroutines.flow.MutableStateFlow
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorViewModel: ViewModel() {

    var state = MutableStateFlow("0")
        private set
    private var isSubmitted = false
    private val error = "Error"

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Clear -> {
                state.value = "0"
            }
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Calculate -> performCalculation()
        }
    }

    private fun performDeletion() {
        if (isSubmitted) return
        var value = state.value
        value = if (value == error) "0" else value.dropLast(1).ifEmpty { "0" }
        isSubmitted = false
        state.value = value
    }

    private fun enterOperation(operation: CalculatorOperation) {
        var value = state.value

        if (value == "0" || value == error) {
            value = "0${operation.symbol}"
        } else if (!value.last().isDigit()) {
            value = value.dropLast(1) + operation.symbol
        } else {
            if (value.contains(Regex("[+\\-x/]"))) {
                performCalculation()
            }
            value += operation.symbol
        }
        isSubmitted = false
        state.value = value.replace(Regex("[*/]")) {
            when (it.value) {
                "*" -> "x"
                else -> "÷"
            }
        }
    }

    private fun enterNumber(number: Int) {
        var value = state.value
        if (value == "0" || value == error) {
            value = "$number"
        } else if (isSubmitted) {
            value = "$number"
        } else {
            value += "$number"
        }
        isSubmitted = false
        state.value = value
    }

    private fun enterDecimal() {
        var value = state.value
        val lastValue = value.splitToSequence(Regex("[+\\-x/]"))
            .lastOrNull()

        if ((lastValue != null) && lastValue.contains(".") && !isSubmitted) {
            return
        }
        if (value == "0" || isSubmitted || value == error) {
            value = "0."
        } else if (value.last().isDigit()) {
            value += "."
        } else {
            value += "0."
        }
        isSubmitted = false
        state.value = value
    }

    private fun performCalculation() {
        var value = state.value.replace(Regex("[x÷]")) {
            when (it.value) {
                "x" -> "*"
                else -> "/"
            }
        }
        val expression: String

        when {
            value.last() == '.' -> value = value.dropLast(1)

            value.last().isLetter() -> value = "0"

            !value.last().isDigit() -> {
                val operator = value.last()
                expression = value.dropLast(1)
                value = expression + operator + expression
            }
        }

        val result = try {
            ExpressionBuilder(value).build().evaluate()
        } catch (e: ArithmeticException) {
            state.value = error
            isSubmitted = true
            return
        }

        value = if (result % 1 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        state.value = value
        isSubmitted = true
    }
}