package ru.easycode.tddcalculator

import java.math.BigInteger

interface MainRepository {

    fun sum(left: String, right: String): String

    class Base : MainRepository {

        override fun sum(left: String, right: String): String {
            return BigInteger(left).plus(BigInteger(right)).toString()
        }
    }
}