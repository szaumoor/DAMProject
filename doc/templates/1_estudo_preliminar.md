# Estudio preliminar

## 1. Descripción del proyecto

El propósito del proyecto es hacer **dos apps** (móvil y desktop, integradas con una base de datos común), denominadas con el nombre **Rumpel**, que permita a cada usuario hacer un seguimiento de gastos que tiene para productos particulares, dentro de categorías/etiquetas relevantes, y a raíz de ello recibir sencillos informes personalizados y periódicos (mensuales o anuales), y ofrecer advertencias y feedback para la toma de decisiones sobre ahorro, control de las finanzas domésticas y hábitos de consumo.

### 1.1. Justificación del proyecto

La aplicación intentará cubrir o contribuir principalmente para las siguientes necesidades, de capital importancia en el día a día de cualquier adulto o unidad familiar:

- **Ahorro**: Esta es una necesidad muy básica que se aplica a toda persona en edad de trabajar que percibe un salario,  pensión o comisión de cualquier tipo, independientemente de su nivel socioeconómico. Es una necesidad muy importante, ya que contribuye fuertemente a la supervivencia y bienestar del individuo.
- **Organización y planificación**: Permite o contribuye a cubrir la necesidad de organizar y planear sus gastos y otras actividades relacionadas al gasto de dinero o consumición de productos.


### 1.2. Funcionalidades del proyecto

Estas son las funcionalidades básicas del proyecto:

1. Realizar un **seguimiento periódico** de gastos.
2. Permitir al usuario **introducir datos manualmente** y opcionalmente (si es viable) permitir **escaneo** de facturas para introducir datos.
3. Mantener un **histórico de gastos** y productos comprados para ofrecer sugerencias a medida que la aplicación tiene acceso a más información.
4. Calcular gastos y realizar **informes** sobre los gastos, usando texto y **gráficos** (de sectores, entre otros), detallando la distribución de gastos a lo largo de un período,  qué categorías tienen más o menos gasto mensual, número de compras, etcétera, la cual se puede comparar con una renta mensual seleccionada o un gasto límite arbitrario o sugerido por la aplicación en vista de información pasada.
5. **Clasificar** productos ordenadamente, y utilizar estas clasificaciones para mejorar los informes.
6. Permitir al usuario hacer **búsquedas** sobre la información almacenada utilizando filtros avanzados, además de poder **pedir informes manualmente** sobre un periodo en particular o sobre una categoría de producto específica.
7. Maximizar la facilidad de inserción y visualización de información para ser lo más **accesible** posible, además de ofrecerse en **español, gallego e inglés**, para empezar.
8. Permitir al usuario acceder a todas las funcionalidades básicas tanto desde su móvil como desde su ordenador personal, garantizando la **sincronización** entre dispositivos.

### 1.3. Estudio de necesidades

Existen otras aplicaciones que proporcionan un servicio similar si miramos en Google Play. Por ejemplo, hay una aplicación llamada Expense Manager - Tracker cuyo autor es CASorin. Tiene funcionalidades similares a las propuestas en este anteproyecto: calendarios, gráficas, cálculos, reportes además de otras características que mejoran la experiencia de usuario, tales como calculadora integrada y recordatorios. Otra aplicación llamada Money Manager Expense & Budget hecha por Realbyte Inc tiene características similares.

Sin embargo, ambas carecen de una integración con una aplicación desktop para Windows, Linux y Mac, lo cual es algo que este proyecto podría introducir: la posibilidad para cualquier cliente de utilizar una versión desktop si prefiere no usar aplicaciones móviles, o ambas, si lo prefiere, con sincronización entre ambas.

### 1.4. Personas destinatarias

Está aplicación va dirigida a prácticamente cualquier persona adulta que regularmente perciba ingresos y afronte gastos. Es especialmente útil para aquellas personas que necesitan realizar ajustes para asegurarse de que pueden llegar a final de mes, pero también para aquellas que quieran planear y analizar sus gastos para maximizar los ahorros, conllevar gastos de forma más inteligente, o detectar posibles pérdidas excesivas de dinero para tomar mejores decisiones. Teniendo esto en cuenta, y considerando que el dinero y el ahorro son importantes para la vida adulta en sociedad, el contexto social para esta aplicación es virtualmente ilimitado. En este sentido, hay pocas limitaciones en cuanto a su alcance social potencial.

### 1.5. Modelo de negocio

Esta aplicación podría fácilmente ofrecerse de forma gratuita para tanto Android (en Google Play) como para otras plataformas desktop con funcionalidades limitadas, con la opción de adquirir una versión completa o “premium”. El potencial total de la aplicación se podría desbloquear con un pago único o recurrente, dependiendo de la situación y tras un análisis previo de la estrategia comercial más apropiada.

Otra posibilidad para obtener ganancia económica sería la introducción de anuncios, que serían desbloqueables para los usuarios que decidan pagar por la versión completa. Sin embargo, a pesar de ser algo potencialmente lucrativo, es también una estrategia que tiende a generar bastante malestar entre los usuarios, con lo cual tendría que considerarse con cuidado e implementarla, si procede de una forma lo menos invasiva o abusiva posible.

En cualquier caso, dado el contexto social previamente delineado, el potencial comercial existe.

## 2. Requerimientos

El lenguaje de programación principal en la aplicación es **Java** (mínimo versión **17**), y **Kotlin** parcialmente en la aplicación de **Android**. Se considerará usar otros lenguages tales como C++, en caso de que su introducción pueda agilizar alguna operación pesada o introducir una librería de utilidad.

Para la aplicación de escritorio, utilizaré **JavaFX** para realizar la interfaz de usuario, utilizando además IDEs avanzados tales como **IntelliJ** y otras herramientas (tales como **SceneBuilder** en el caso de JavaFX).

La aplicación móvil, que será para **Android**, utilizará el propio ecosistema de Android y todas las herramientas que IDES para Android tales como **Android Studio** proporcionan para ese propósito. Todo esto implica el uso de otras tecnologías tangenciales tales como **Maven/Gradle**, recursos **XML**, **Jetpack Compose**, además de otras herramientas que permitan la creación de diseños de interfaz en Android, entre otros. En esta aplicación, se utilizará si es viable además las librerías de Google para el reconocimiento de texto usando **Machine Learning** a través de la cámara del móvil, para así reconocer texto en facturas.

Dado que el proyecto implica interacciones con una base de datos, se utilizará **MongoDB**. Esta base de datos además estaría alojada en la nube, utilizando Mongo **Atlas**, haciendo uso de las varias herramientas para interactuar, tales como el SDK de Java y Android, Realms, Device Sync, autenticación, y otras tecnologías.

Adicionalmente, se usarán librerías de para implementar tests de funcionalidad en el código, para evitar que hayan **regresiones** en el programa al añadir nuevas características. Para ello, utilizaré librerías para realización de tests funcionales tales como **JUnit**, además de las herramientas propias de los IDE para realizar el testeo, lo cuál facilitará la labor y manejo de errores, además de posibles casos extremos (**edge cases**).

Por último, utilizaré **Git** para manejar el control de versiones, creando las ramas necesarias para evitar commits inoportunos en la rama principal.
