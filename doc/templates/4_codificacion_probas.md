# Codificación y pruebas

## Codificación

La codificación presentó variados problemas que tuvieron que resolverse:

- Ajustar trabajo de **hilos**, asegurándose que los hilos de la interfaz de usuario y los hilos de trabajo en segundo plano no entren en conflicto, se bloqueen uno a otro, u ocurran _race conditions_ entre las diversas variables a modificar.
- Investigación sobre el funcionamiento de los **módulos**, presentes desde Java 9, y parte del proyecto de JavaFX.
- Investigación sobre cómo exportar librerías **JAR** de proyectos personales para su uso en aplicaciones de escritorio y móvil.
- Búsqueda de un servicio de bases de datos en la nube que ofrezca una API y funcionalidad apropiada, y adaptación con el código del modelo, habiendo probado servicios como **DynamoDB**, **Azure** y finalmente **MongoDB**.
- Diseño de paquetes y clases de Java para lograr un diseño coherente con el menor duplicado de código posible, y la mayor reutilización de código a través de la herencia y funciones encapsulando funcionalidad genérica o común.
- Separación de las clases **controladores**, de **vistas**, y de **modelo** de forma coherente, además de otras clases de utilidades.
- Resolución de dificultades en codificación en el entorno Android, al usar una librería y una forma de codificar considerablemente diferente de las aplicaciones de escritorio.
- Estudio de **Kotlin** y **Jetpack Compose**, dos herramientas con las que no tenía familiaridad para codificar y crear interfaces de usuario.

## Prototipos

### <u>Prototipo 1</u>

#### Escritorio
Funcionalidades:
- Implementada conexión a MongoDB y logueado utilizando datos de cloud y encriptación de contraseñas con BCrypt.
- Implementados aspectos visuales "avanzados" utilizando librerías de Bootstrap,
- Inserción de nuevos usuarios (registrado) en la base de datos, utilizando verificación para evitar usuarios inválidos.
- Inserción y consultas sobre presupuestos, etiquetas y métodos de pago.
- Implementada las vistas básicas tras el logueado en la aplicación.

Problemas:
- La conexión a MongoDB está expuesta en el código de la aplicación de escritorio. Actualmente es relativamente simple decompilar una aplicación y averiguar el contenido de la cadena de texto conteniendo la dirección para conectarse a Mongo.
- Falta de tema oscuro.
- Falta de medidas de seguridad tales como verificación de usuario a través de email.

#### Android
Funcionalidades:
- Implementada conexión a MongoDB y logueado utilizando datos de cloud y encriptación de contraseñas con BCrypt.
- Inserción de nuevos usuarios (registrado) en la base de datos.
- Consultas a la base de datos de etiquetas y métodos de pago, además de inserciones.
- Implementada las vistas (muy básicas) de logueado, registrado, menu y la pantalla principal, con datos de prueba. Pendiente de revisar profundamente y completar.

Problemas:
- Falta de tema oscuro.
- Falta de medidas de seguridad tales como verificación de usuario a través de email.
- Sin uso de usuarios de aplicación que es parte de MongoDB. En su lugar, actualmente se usan usuarios metidos en una colección dentro de la base de datos.
- Falta de sincronización entre el hilo de IU y procesos sobre la base de datos (bloqueo de la UI, Android podría teoréticamente pedir que se mate a la aplicación si tarda demasiado).

### <u>Prototipo 2</u>

#### Escritorio
Funcionalidades:
- Implementadas todas las operaciones CRUD con Mongo restantes para todas las entidades de la base de datos.
- Añadidos más botones y menús contextuales para mejorar la experiencia de usuario.
- Implementados botones de refrescado para resolver cualquier problema de sincronización sin tener que reiniciar la app.
- Implementada funcionalidad para cambiar de contraseña.
- Empezada la codificación de clases para encargarse de los informes, además de una prueba de concepto implementada, que muestra un par de gráficas, una de test y otra basadas en la distribución de gastos por etiqueta existente en la base de datos.

Problemas:
- La conexión a MongoDB sigue expuesta en el código.
- Falta de tema oscuro.
- Falta de medidas de seguridad tales como verificación de usuario a través de email.

Funcionalidades pendientes de completar:
- Completar informes, funcionalidad y customización
- Extender el uso de hilos para eliminar otros bloqueos menores aún no manejados
- Resolver conflictos de monedas de forma que se puedan crear informes significativos independientemente de la moneda usada en gastos
- Falta la implementación de borrado de usuarios, con todo lo que conlleva (borrado selectivo en la base de datos)
- Añadir una forma de ver gastos dentro de franjas temporales, con la posibilidad de cambiar a otras, en lugar de ver todos los gastos insertados desde el inicio de los tiempos.

#### Android
Funcionalidades:
- Implementada funcionalidad para realizar tareas de fondo sin interrumpir el hilo principal (por tanto, evitando que la aplicación se bloquee, o Android cuelgue la aplicación)
- La mayoría de vistas hechas de nuevo usando Kotlin y Jetpack Compose, cambiando el código pero manteniendo la funcionalidad original.
- Añadidos operaciones CRUD con más entidades, aún sin completar.

Problemas:
- Falta de tema oscuro.
- Falta de medidas de seguridad tales como verificación de usuario a través de email.

Funcionalidades pendientes de completar:
- Completar las vistas, y a ser posible encontrando recursos online on Jetpack Composables sofisticados para mejorar la experiencia visual.
- Buscar e implementar recursos para añadir gráficas con Jetpack Compose u otros recursos para Android.
- Internacionalizar todo el texto.
- Uso de Machine Learning para detectar texto en imágenes (facturas)

### <u>Prototipo final</u>

##### Estado de las funcionalidades

<table>
  <tr>
    <th>Funcionalidad</th>
    <th>En escritorio</th>
    <th>En Android</th>
  </tr>
  <tr>
    <td>Alta de cliente</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Logueado de cliente</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Modificar cliente</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Borrar cliente</td>
    <td>Presente</td>
    <td>Ausente</td>
  </tr>
  <tr>
    <td>Añadir gasto</td>
    <td>Presente</td>
    <td>Presente, escaneado con cámara sin implementación funcional</td>
  </tr>
  <tr>
    <td>Modificar gasto</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Eliminar gasto</td>
    <td>Presente</td>
    <td>Presente, aunque implementado de forma muy básica</td>
  </tr>
  <tr>
    <td>Añadir etiqueta</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Modificar etiqueta</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Eliminar etiqueta</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Añadir método de pago</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Modificar método de pago</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Eliminar método de pago</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Añadir presupuesto</td>
    <td>Presente</td>
    <td>Presente, feedback ausente</td>
  </tr>
  <tr>
    <td>Modificar presupuesto</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Eliminar presupuesto</td>
    <td>Presente</td>
    <td>Presente</td>
  </tr>
  <tr>
    <td>Creación de informes</td>
    <td>Presente</td>
    <td>Presente</td>
</table>

## Innovación

En cuanto a la innovación o elementos no vistos en el curso:

- Se tuvo que investigar cómo usar **Mongo Realms** para aplicaciones de móvil y como sincronizar este uso con la forma de trabajar en aplicaciones de escritorio conectadas a Mongo, usando variadas funcionalidades tales como Device Sync y otras configuraciones en la nube.
- Se usaron librerías Java y JavaFX para aumentar la funcionalidad y diseño, tales como BootstrapFX, JMail (validación de emails), password4j (encriptación y desencriptación) y log4j (logging robusto de la funcionalidad de la aplicación), facilitando enormemente ciertas tareas.
- Se usó **TDD** (_test-driven development_) y **JUnit** para pruebas de funcionalidad.
- Aplicada internacionalización de texto en tres idiomas en la aplicación de escritorio a través de recursos.
- Uso mayoritario de **Kotlin** y **Jetpack Compose** para la creación de las vistas y actividades en Android, en lugar del típico uso de recursos de XML y ids, y otros aspectos menores.
- Uso más avanzado de hilos para la división de tareas en fragmentos, aumentando el rendimiento considerablemente.

## Pruebas

Se utilizaron varias técnicas para detectar y resolver problemas encontrados:

- **IntelliJ** fue muy importante para detectar errores de variados tipos, tales como variables sin uso, problemas de exportación en el módulo de Java, importe de librerías, problemas en potencia, entre otras cosas.
- Uso de excepciones significativas para detectar problemas.
- Uso de logging en la consola a través de la API base de Java y más tarde el uso de log4j para hacer pruebas y detectar errores en el flujo del programa.
- Se utilizó el depurador propio de IntelliJ para detectar varios problemas, en el que cabe destacar problemas en la administración de hilos.
- Herramientas de inteligencia artificial y foros para probar soluciones y resolver variados problemas que hubo con hilos y el uso de MongoDB en diversas plataformas.
- Adaptación del código para el uso tanto en Android como en escritorio.
- Uso de pruebas de código utilizando las librerías de **JUnit** de Java en IntelliJ para probar funcionalidades, particularmente para poner a prueba el modelo para ver lo robusto que es.
- Uso de *previews* para Jetpack Compose a medida que construía las funciones, reduciendo errores, problemas de interfaz, y tiempo de pruebas.
