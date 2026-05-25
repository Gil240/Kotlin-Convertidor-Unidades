package com.example.kotlinconvertidorunidades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinconvertidorunidades.domain.UnitConversionCatalog
import com.example.kotlinconvertidorunidades.ui.ConverterHomeScreen
import com.example.kotlinconvertidorunidades.ui.theme.KotlinConvertidorUnidadesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinConvertidorUnidadesTheme {
                ConverterHomeScreen(
                    categories = UnitConversionCatalog.getVisibleCategories(),
                    unitSymbolsByCategory = UnitConversionCatalog.getUnitSymbolsByCategory()
                )
            }
        }
    }
}
