package ru.easycode.tddcalculator

import java.math.BigDecimal
import java.math.BigInteger

interface MainRepository {

    fun sum(left: String, right: String): String

    fun diff(left: String, right: String): String

    fun multiply(left: String, right: String): String

    fun divide(left: String, right: String): String

    class Base : MainRepository {

        override fun sum(left: String, right: String): String {
            return BigInteger(left).plus(BigInteger(right)).toString()
        }

        override fun diff(left: String, right: String): String {
            return BigInteger(left).minus(BigInteger(right)).toString()
        }

        override fun multiply(left: String, right: String): String {
            return BigInteger(left).multiply(BigInteger(right)).toString()
        }

        override fun divide(left: String, right: String): String {
            val result = BigDecimal(left).divide(BigDecimal(right))
            return result.toString()
        }
    }
}