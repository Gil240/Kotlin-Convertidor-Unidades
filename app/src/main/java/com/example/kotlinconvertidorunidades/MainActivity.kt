package com.example.kotlinconvertidorunidades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinconvertidorunidades.domain.UnitConversionCatalog
import com.example.kotlinconvertidorunidades.ui.theme.KotlinConvertidorUnidadesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinConvertidorUnidadesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConverterHomeScreen(
                        categorySummaries = UnitConversionCatalog.getCategorySummaries(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ConverterHomeScreen(
    categorySummaries: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Convertidor de unidades",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Catalogo base y motor de conversiones listo.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Categorias disponibles",
            style = MaterialTheme.typography.titleMedium
        )
        categorySummaries.forEach { summary ->
            Text(
                text = "- $summary",
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
            categorySummaries = listOf(
                "Temperatura: C, F, K",
                "Longitud: m, km, cm, mi"
            )
        )
    }
}
