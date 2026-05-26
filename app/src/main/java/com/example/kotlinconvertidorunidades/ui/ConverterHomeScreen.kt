package com.example.kotlinconvertidorunidades.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinconvertidorunidades.domain.ConversionCategory
import com.example.kotlinconvertidorunidades.domain.ConversionResponse
import com.example.kotlinconvertidorunidades.domain.UnitDefinition
import com.example.kotlinconvertidorunidades.domain.UnitConversionCatalog
import com.example.kotlinconvertidorunidades.ui.theme.KotlinConvertidorUnidadesTheme
import java.util.Locale

@Composable
fun ConverterHomeScreen(
    categories: List<ConversionCategory>,
    unitsByCategory: Map<String, List<UnitDefinition>>,
    modifier: Modifier = Modifier
) {
    var selectedCategoryId by rememberSaveable {
        mutableStateOf(categories.firstOrNull()?.id.orEmpty())
    }
    var fromUnitSymbol by rememberSaveable {
        val firstUnits = unitsByCategory[selectedCategoryId] ?: emptyList()
        mutableStateOf(firstUnits.firstOrNull()?.symbol.orEmpty())
    }
    var toUnitSymbol by rememberSaveable {
        val firstUnits = unitsByCategory[selectedCategoryId] ?: emptyList()
        mutableStateOf(firstUnits.getOrNull(1)?.symbol ?: firstUnits.firstOrNull()?.symbol.orEmpty())
    }
    var inputText by rememberSaveable {
        mutableStateOf("")
    }

    val selectedCategory = categories.firstOrNull { category ->
        category.id == selectedCategoryId
    } ?: categories.firstOrNull()
    val selectedUnits = unitsByCategory[selectedCategory?.id].orEmpty()
    val selectedFromUnit = selectedUnits.firstOrNull { unit ->
        unit.symbol == fromUnitSymbol
    } ?: selectedUnits.firstOrNull()
    val selectedToUnit = selectedUnits.firstOrNull { unit ->
        unit.symbol == toUnitSymbol
    } ?: selectedUnits.getOrNull(1) ?: selectedUnits.firstOrNull()
    val conversionResponse = if (inputText.isBlank()) {
        null
    } else {
        UnitConversionCatalog.convertValue(
            inputText = inputText,
            fromUnitQuery = selectedFromUnit?.symbol,
            toUnitQuery = selectedToUnit?.symbol,
            categoryId = selectedCategory?.id
        )
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HeaderSection(totalCategories = categories.size)
                }

                item {
                    ConversionSelectionSection(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        selectedUnits = selectedUnits,
                        selectedFromUnit = selectedFromUnit,
                        selectedToUnit = selectedToUnit,
                        inputText = inputText,
                        conversionResponse = conversionResponse,
                        onCategorySelected = { category ->
                            selectedCategoryId = category.id
                            val newUnits = unitsByCategory[category.id].orEmpty()
                            fromUnitSymbol = newUnits.firstOrNull()?.symbol.orEmpty()
                            toUnitSymbol = newUnits.getOrNull(1)?.symbol
                                ?: newUnits.firstOrNull()?.symbol.orEmpty()
                        },
                        onFromUnitSelected = { unit ->
                            fromUnitSymbol = unit.symbol
                        },
                        onToUnitSelected = { unit ->
                            toUnitSymbol = unit.symbol
                        },
                        onInputChanged = { newInput ->
                            inputText = newInput
                        }
                    )
                }

                item {
                    Text(
                        text = "Categorias",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                categories.forEach { category ->
                    item(key = category.id) {
                        CategoryCard(
                            category = category,
                            unitSymbols = unitsByCategory[category.id]
                                ?.map { unit -> unit.symbol }
                                ?: emptyList()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(totalCategories: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Convertidor de unidades",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Temperatura, longitud, peso y moneda",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$totalCategories categorias disponibles",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ConversionSelectionSection(
    categories: List<ConversionCategory>,
    selectedCategory: ConversionCategory?,
    selectedUnits: List<UnitDefinition>,
    selectedFromUnit: UnitDefinition?,
    selectedToUnit: UnitDefinition?,
    inputText: String,
    conversionResponse: ConversionResponse?,
    onCategorySelected: (ConversionCategory) -> Unit,
    onFromUnitSelected: (UnitDefinition) -> Unit,
    onToUnitSelected: (UnitDefinition) -> Unit,
    onInputChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Seleccion de conversion",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = selectedCategory?.description ?: "Agrega categorias para comenzar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            OptionDropdown(
                label = "Categoria",
                selectedText = selectedCategory?.title ?: "Sin categorias",
                options = categories,
                optionText = { category -> category.title },
                onOptionSelected = onCategorySelected
            )

            OptionDropdown(
                label = "Convertir de",
                selectedText = selectedFromUnit?.displayName() ?: "Sin unidades",
                options = selectedUnits,
                optionText = { unit -> unit.displayName() },
                onOptionSelected = onFromUnitSelected
            )

            OptionDropdown(
                label = "Convertir a",
                selectedText = selectedToUnit?.displayName() ?: "Sin unidades",
                options = selectedUnits,
                optionText = { unit -> unit.displayName() },
                onOptionSelected = onToUnitSelected
            )

            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Valor")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            ConversionResultCard(
                inputText = inputText,
                selectedFromUnit = selectedFromUnit,
                selectedToUnit = selectedToUnit,
                conversionResponse = conversionResponse
            )
        }
    }
}

@Composable
private fun ConversionResultCard(
    inputText: String,
    selectedFromUnit: UnitDefinition?,
    selectedToUnit: UnitDefinition?,
    conversionResponse: ConversionResponse?
) {
    val containerColor = when (conversionResponse) {
        is ConversionResponse.Error -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val contentColor = when (conversionResponse) {
        is ConversionResponse.Error -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Resultado",
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )

            when (conversionResponse) {
                is ConversionResponse.Success -> {
                    val result = conversionResponse.result

                    Text(
                        text = "${formatNumber(result.originalValue)} ${result.fromUnit.symbol} = ${formatNumber(result.convertedValue)} ${result.toUnit.symbol}",
                        style = MaterialTheme.typography.titleMedium,
                        color = contentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${result.fromUnit.name} a ${result.toUnit.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }

                is ConversionResponse.Error -> {
                    Text(
                        text = conversionResponse.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }

                null -> {
                    val fromSymbol = selectedFromUnit?.symbol ?: "-"
                    val toSymbol = selectedToUnit?.symbol ?: "-"
                    val helperText = if (inputText.isBlank()) {
                        "Escribe un valor para convertir de $fromSymbol a $toSymbol."
                    } else {
                        "Selecciona unidades validas para continuar."
                    }

                    Text(
                        text = helperText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> OptionDropdown(
    label: String,
    selectedText: String,
    options: List<T>,
    optionText: (T) -> String,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = selectedText,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (expanded) "^" else "v",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = optionText(option))
                        },
                        onClick = {
                            expanded = false
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }
    }
}

private fun UnitDefinition.displayName(): String {
    return "$name ($symbol)"
}

private fun formatNumber(value: Double): String {
    return String.format(Locale.US, "%.4f", value)
        .trimEnd('0')
        .trimEnd('.')
}

@Composable
private fun CategoryCard(
    category: ConversionCategory,
    unitSymbols: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${unitSymbols.size} unidades",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = unitSymbols.joinToString(", "),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConverterHomeScreenPreview() {
    KotlinConvertidorUnidadesTheme {
        ConverterHomeScreen(
            categories = UnitConversionCatalog.getVisibleCategories(),
            unitsByCategory = UnitConversionCatalog.getUnitsByCategory()
        )
    }
}
