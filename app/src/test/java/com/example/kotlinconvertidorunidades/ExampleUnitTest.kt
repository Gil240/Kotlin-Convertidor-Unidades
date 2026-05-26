package com.example.kotlinconvertidorunidades

import com.example.kotlinconvertidorunidades.domain.ConversionResponse
import com.example.kotlinconvertidorunidades.domain.UnitConversionCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun convertCelsiusToFahrenheitReturnsExpectedValue() {
        val response = UnitConversionCatalog.convertValue(
            inputText = "100",
            fromUnitQuery = "C",
            toUnitQuery = "F",
            categoryId = "temperatura"
        )

        when (response) {
            is ConversionResponse.Success -> {
                assertEquals(212.0, response.result.convertedValue, 0.001)
            }

            is ConversionResponse.Error -> {
                fail(response.message)
            }
        }
    }

    @Test
    fun catalogFindsUnitsUsingSafeSearch() {
        val lengthUnits = UnitConversionCatalog.getUnitsForCategory("longitud")
        val symbols = lengthUnits.map { unit -> unit.symbol }

        assertTrue(symbols.contains("km"))
        assertTrue(symbols.contains("ft"))
        assertTrue(UnitConversionCatalog.categoryHasUnit("peso", "lb"))
    }

    @Test
    fun convertMetersToFeetReturnsExpectedValue() {
        val response = UnitConversionCatalog.convertValue(
            inputText = "1",
            fromUnitQuery = "m",
            toUnitQuery = "ft",
            categoryId = "longitud"
        )

        when (response) {
            is ConversionResponse.Success -> {
                assertEquals(3.2808, response.result.convertedValue, 0.001)
            }

            is ConversionResponse.Error -> {
                fail(response.message)
            }
        }
    }

    @Test
    fun convertKilogramsToOuncesReturnsExpectedValue() {
        val response = UnitConversionCatalog.convertValue(
            inputText = "1",
            fromUnitQuery = "kg",
            toUnitQuery = "oz",
            categoryId = "peso"
        )

        when (response) {
            is ConversionResponse.Success -> {
                assertEquals(35.274, response.result.convertedValue, 0.001)
            }

            is ConversionResponse.Error -> {
                fail(response.message)
            }
        }
    }

    @Test
    fun convertCurrencyWithFixedPracticeRate() {
        val response = UnitConversionCatalog.convertValue(
            inputText = "34",
            fromUnitQuery = "MXN",
            toUnitQuery = "USD",
            categoryId = "moneda"
        )

        when (response) {
            is ConversionResponse.Success -> {
                assertEquals(2.0, response.result.convertedValue, 0.001)
            }

            is ConversionResponse.Error -> {
                fail(response.message)
            }
        }
    }

    @Test
    fun invalidInputReturnsErrorWithoutCrash() {
        val response = UnitConversionCatalog.convertValue(
            inputText = "abc",
            fromUnitQuery = "C",
            toUnitQuery = "F",
            categoryId = "temperatura"
        )

        assertTrue(response is ConversionResponse.Error)
    }
}
