# Manuales
## Manual técnico del proyecto
### Instalación
Para continuar el desarrollo de este proyecto basta con descargarlo, y abrirlo en un IDE que maneje dependencias con **Maven** y **Gradle** tales como **IntelliJ** y **Android Studio**, para que se descarguen y el proyecto funcione. Será necesario modificar los datos de conexión a la base de datos, después de crear una base de datos **MongoDB** con los nombres apropiados, incluidas las colecciones, a menos que se desee cambiar los nombres de las entidades y otros aspectos.

Para instalar lo que es la aplicación en sí, es simplemente necesario ejecutar, en el caso de la versión de escritorio, el **JAR** provisto con el comando **"java -jar \<nombre del jar>"**, o también se puede reconstruir por propia cuenta. La versión provista debe mantener su estructura de archivos, en caso contrario, el comando anterior no funcionará.

En el caso de la versión de Android basta con construir el proyecto desde la interfaz de Android Studio tras descargar todas las dependencias y construir el proyecto satisfactoriamente, ya sea en versión **Debug** o **Release**.

## Mejoras futuras

Hay varias cosas mejorables para hacer el proyecto más profesional o agradable para el usuario, ya sea en materia de accesibilidad o agrado visual:

- Añadir un **tema oscuro** decente funcional para ambas versiones, para el beneficio de aquellas personas con sensibilidad a las luces o que quiera limitar el consumo de energía.
- Añadir un tema específico para gente daltónica.
- Añadir pequeños **tutoriales** interactivos de uso dentro de las aplicaciones.
- Autenticación profesional a través de Google y otros sistemas de autenticación que sincronice cuentas con direcciones de correo electrónico que mejore la seguridad y disminuya abusos, más allá de la encriptación de las contraseñas. Esto necesitaría, en el caso de **MongoDB**, una suscripción a un **cluster de pago**.
- Rediseño de la interfaz de usuario con aspectos modernos tales como animaciones y distribuciones cromáticas más agradables o memorables.
- Refrescado automático de la información en la nube para mejorar la experiencia de usuario.
- Usar elementos en recibos insertados previamente para mejorar la consistencia de datos y añadir funcionalidades tales como predicciones para nuevos futuros elementos, además de informes que tenga en cuenta elementos particularles.
- Expandir la variedad de informes y aumentar la customización de los informes.
- Añadir la posibilidad de añadir gastos mes a mes automáticamente, para gastos con recurrencia constante (antivirus, Netflix, Photoshop, etc)
- Añadir la posibilidad de añadir datos en modo sin internet
