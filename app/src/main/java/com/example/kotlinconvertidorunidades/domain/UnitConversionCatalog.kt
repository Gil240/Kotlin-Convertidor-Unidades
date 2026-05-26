package com.example.kotlinconvertidorunidades.domain

import java.util.Locale

data class ConversionCategory(
    val id: String,
    val title: String,
    val description: String
)

data class UnitDefinition(
    val categoryId: String,
    val name: String,
    val symbol: String,
    val toBaseValue: (Double) -> Double,
    val fromBaseValue: (Double) -> Double
) {
    fun matches(query: String?): Boolean {
        val normalizedQuery = query?.trim()?.lowercase(Locale.US) ?: return false

        if (normalizedQuery.isEmpty()) {
            return false
        }

        return symbol.lowercase(Locale.US) == normalizedQuery ||
            name.lowercase(Locale.US) == normalizedQuery
    }
}

data class ConversionResult(
    val originalValue: Double,
    val convertedValue: Double,
    val fromUnit: UnitDefinition,
    val toUnit: UnitDefinition,
    val category: ConversionCategory
)

sealed class ConversionResponse {
    data class Success(val result: ConversionResult) : ConversionResponse()
    data class Error(val message: String) : ConversionResponse()
}

object UnitConversionCatalog {
    private const val TEMPERATURE = "temperatura"
    private const val LENGTH = "longitud"
    private const val WEIGHT = "peso"
    private const val CURRENCY = "moneda"

    val categories: List<ConversionCategory> = listOf(
        ConversionCategory(TEMPERATURE, "Temperatura", "Celsius, Fahrenheit y Kelvin"),
        ConversionCategory(LENGTH, "Longitud", "Metros, kilometros, centimetros y millas"),
        ConversionCategory(WEIGHT, "Peso", "Kilogramos, gramos y libras"),
        ConversionCategory(CURRENCY, "Moneda", "Tasas fijas de ejemplo para uso escolar")
    )

    val units: List<UnitDefinition> = listOf(
        UnitDefinition(TEMPERATURE, "Celsius", "C", { value -> value }, { value -> value }),
        UnitDefinition(TEMPERATURE, "Fahrenheit", "F", { value -> (value - 32.0) * 5.0 / 9.0 }, { value -> value * 9.0 / 5.0 + 32.0 }),
        UnitDefinition(TEMPERATURE, "Kelvin", "K", { value -> value - 273.15 }, { value -> value + 273.15 }),
        UnitDefinition(LENGTH, "Metro", "m", { value -> value }, { value -> value }),
        UnitDefinition(LENGTH, "Kilometro", "km", { value -> value * 1_000.0 }, { value -> value / 1_000.0 }),
        UnitDefinition(LENGTH, "Centimetro", "cm", { value -> value / 100.0 }, { value -> value * 100.0 }),
        UnitDefinition(LENGTH, "Milla", "mi", { value -> value * 1_609.344 }, { value -> value / 1_609.344 }),
        UnitDefinition(WEIGHT, "Kilogramo", "kg", { value -> value }, { value -> value }),
        UnitDefinition(WEIGHT, "Gramo", "g", { value -> value / 1_000.0 }, { value -> value * 1_000.0 }),
        UnitDefinition(WEIGHT, "Libra", "lb", { value -> value * 0.453_592_37 }, { value -> value / 0.453_592_37 }),
        UnitDefinition(CURRENCY, "Peso mexicano", "MXN", { value -> value }, { value -> value }),
        UnitDefinition(CURRENCY, "Dolar estadounidense", "USD", { value -> value * 17.0 }, { value -> value / 17.0 }),
        UnitDefinition(CURRENCY, "Euro", "EUR", { value -> value * 18.5 }, { value -> value / 18.5 })
    )

    private val unitsByCategory: Map<String, List<UnitDefinition>> = units.groupBy { unit ->
        unit.categoryId
    }

    fun getVisibleCategories(): List<ConversionCategory> {
        return categories.filter { category ->
            unitsByCategory[category.id]?.isNotEmpty() ?: false
        }
    }

    fun getUnitsForCategory(categoryId: String?): List<UnitDefinition> {
        val normalizedCategoryId = normalizeText(categoryId) ?: return emptyList()

        return unitsByCategory[normalizedCategoryId]
            ?.filter { unit -> unit.categoryId == normalizedCategoryId }
            ?: emptyList()
    }

    fun getCategorySummaries(): List<String> {
        val summaries = mutableListOf<String>()

        for (category in getVisibleCategories()) {
            val symbols = getUnitsForCategory(category.id)
                .map { unit -> unit.symbol }
                .joinToString(", ")

            summaries.add("${category.title}: $symbols")
        }

        return summaries
    }

    fun getUnitSymbolsByCategory(): Map<String, List<String>> {
        return unitsByCategory.mapValues { entry ->
            entry.value.map { unit -> unit.symbol }
        }
    }

    fun getUnitsByCategory(): Map<String, List<UnitDefinition>> {
        return unitsByCategory.mapValues { entry ->
            entry.value
        }
    }

    fun categoryHasUnit(categoryId: String?, unitQuery: String?): Boolean {
        return getUnitsForCategory(categoryId).any { unit ->
            unit.matches(unitQuery)
        }
    }

    fun convertValue(
        inputText: String?,
        fromUnitQuery: String?,
        toUnitQuery: String?,
        categoryId: String?
    ): ConversionResponse {
        val valueToConvert = inputText
            ?.trim()
            ?.replace(",", ".")
            ?.toDoubleOrNull()
            ?: return ConversionResponse.Error("Ingresa un numero valido.")

        val fromUnit = findUnit(categoryId, fromUnitQuery)
        val toUnit = findUnit(categoryId, toUnitQuery)

        return when {
            fromUnit == null -> ConversionResponse.Error("Unidad inicial no encontrada.")
            toUnit == null -> ConversionResponse.Error("Unidad final no encontrada.")
            fromUnit.categoryId != toUnit.categoryId -> ConversionResponse.Error("Las unidades pertenecen a categorias distintas.")
            else -> {
                val baseValue = fromUnit.toBaseValue(valueToConvert)
                val convertedValue = toUnit.fromBaseValue(baseValue)
                val category = findCategoryById(fromUnit.categoryId)
                    ?: ConversionCategory(fromUnit.categoryId, "Categoria", "Categoria personalizada")

                ConversionResponse.Success(
                    ConversionResult(
                        originalValue = valueToConvert,
                        convertedValue = convertedValue,
                        fromUnit = fromUnit,
                        toUnit = toUnit,
                        category = category
                    )
                )
            }
        }
    }

    fun findCategoryById(categoryId: String?): ConversionCategory? {
        val normalizedCategoryId = normalizeText(categoryId) ?: return null

        return categories.firstOrNull { category ->
            category.id == normalizedCategoryId
        }
    }

    private fun findUnit(categoryId: String?, unitQuery: String?): UnitDefinition? {
        val normalizedCategoryId = normalizeText(categoryId)
        val normalizedUnitQuery = normalizeText(unitQuery) ?: return null
        val candidates = normalizedCategoryId?.let { safeCategoryId ->
            getUnitsForCategory(safeCategoryId)
        } ?: units

        return candidates.firstOrNull { unit ->
            unit.matches(normalizedUnitQuery)
        }
    }

    private fun normalizeText(text: String?): String? {
        val normalizedText = text?.trim()?.lowercase(Locale.US)

        return if (normalizedText.isNullOrEmpty()) {
            null
        } else {
            normalizedText
        }
    }
}
