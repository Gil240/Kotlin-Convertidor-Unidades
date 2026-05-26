Convertidor de Unidades Kotlin

Permite transformar distintos tipos de unidades mediante categorías predeterminadas

Instrucciones para instalacion y ejecucion:
1. Instalar y configurar Android Studio
2. Seleccionar la opción "Clone Repository" en el gestor de proyectos de Android Studio
3. Introducir en el campo URL: https://github.com/Gil240/Kotlin-Convertidor-Unidades.git
4. Seleccionar la opción "Clone"
5. Ejecutar el proyecto en dispositivo Android (Virtual o físico)

Funcionalidades principales
1. Conversión de unidades de temperatura
2. Conversión de unidades de longitud
3. Conversión de unidades de peso
4. Conversión de monedas

Conceptos de Kotlin:

1. Funciones propias con nombres descriptivos: UnitConversionCatalog.kt Funciones como convertValue, getUnitsForCategory, findCategoryById, categoryHasUnit y normalizeText.
2. La lógica no está toda en MainActivity: MainActivity.kt y UnitConversionCatalog.kt MainActivity solo carga la interfaz. La lógica de conversiones está separada en UnitConversionCatalog.
3. Uso de if: normalizeText, matches, ConverterHomeScreen	Se usa para validar textos vacíos, entradas inválidas y estados de la interfaz.
4. Uso de when: convertValue, ConversionResultCard Se usa para decidir si la conversión fue exitosa o si hubo error.
5. Uso de for: getCategorySummaries Recorre las categorías visibles para crear resúmenes.
6. Uso de List: categories, units	Se almacenan listas de categorías y unidades disponibles.
7. Uso de Map:	unitsByCategory, getUnitsByCategory	Agrupa las unidades por categoría.
8. Uso de filter:	getVisibleCategories, getUnitsForCategory	Filtra categorías y unidades válidas.
9. Uso de forEach: ConverterHomeScreen.ktSe usa para mostrar categorías y opciones en la interfaz.
10. Uso de groupBy:	unitsByCategory	Agrupa todas las unidades según su categoría.
11. Manejo seguro de nulos con ?:	matches, convertValue, ConverterHomeScreen	Se accede a valores posiblemente nulos sin provocar errores.
12. Uso del operador Elvis ?:	convertValue, getUnitsForCategory, ConverterHomeScreen	Da valores alternativos cuando algo es nulo.
13. data class:	ConversionCategory, UnitDefinition, ConversionResult	Modelan conceptos del dominio: categorías, unidades y resultados.

a) ¿Qué fue lo más difícil de este proyecto y cómo lo resolviste? 

El manejo de nulos, gracias a que existen muchas posibilidades al momento de permitir al usuario generar una entrada, por lo que debimos considerar todos los casos posibles y las acciones que tomaríamos con respecto a ellos.

b) ¿Hubo algún concepto de Kotlin que al principio no entendías y que ahora sí
comprendes? ¿Cómo llegaste a entenderlo? 

El uso y funcionamiento de las data class, las cuales ayudan a que la información no esté suelta en variables separadas, sino agrupada en objetos claros y reutilizables. Para entenderlo modelamos los datos importantes de la aplicación como la agrupación por categorías y la representación de las distintas unidades.

c) Si tuvieras que mejorar o ampliar este proyecto, ¿qué le agregarías y por qué? 

Agregaríamos funciones relacionadas con el uso e interacción de contenido proveniente de la web, gracias a que esto es uno de los temas mas influyentes en el desarrollo de aplicaciones móviles en la actualidad.

d) ¿Qué aprendiste de este proyecto que no aprendiste solo leyendo o viendo videos?

El como se organizan e interactúan los archivos entre si para permitir a la aplicación funcionar en conjunto, gracias a que esto fue una situación  que dependia mucho del contexto de nuestro proyecto, el enfoque que queríamos darle y el objetivo a cumplir.
