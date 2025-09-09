Meli Challenge App

Esta es una aplicación Android desarrollada en Kotlin con Jetpack Compose y Hilt. Permite buscar productos contra un backend, mostrar los resultados, mantener las últimas búsquedas del usuario y ofrecer una experiencia de uso más completa con sugerencias y persistencia de datos.

Estructura general

- ProductRepository: capa de acceso a datos.

- ProductViewModel: maneja el estado de búsqueda y las sugerencias.

- SearchPreferences: persistencia de las últimas búsquedas.

- UiState: representación del estado de la UI.

- ProductSearchScreen: composable principal con la lógica de búsqueda y visualización.

🛠️ Tecnologías usadas

- Kotlin

- Jetpack Compose

- Hilt
 (inyección de dependencias)

- DataStore Preferences
 (persistencia ligera)

- StateFlow
 (manejo de estado reactivo)

- Coil
 (carga de imágenes)
