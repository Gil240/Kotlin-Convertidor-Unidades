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
        assertTrue(UnitConversionCatalog.categoryHasUnit("peso", "lb"))
    }
}
