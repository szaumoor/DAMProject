# Análisis: Requerimientos del sistema

## Descripción general
Este proyecto consiste en dos aplicaciones -- una móvil, y otra de escritorio -- integrados bajo una base de datos común, que permiten a las personas usuarias almacenar información sobre sus gastos de varias formas, además recibir informes estadísticos sobre tales gastos para ayudarlas a la toma de decisiones.

## Funcionalidades

1. **Alta de cliente**: El cliente de la aplicación podrá darse de alta manualmente mediante un formulario de registro, para el cuál solo se requerirá un nombre de usuario/a, email y contraseña.
2. **Logueado de cliente**: El cliente de la aplicación podrá loguearse mediante una pantalla principal de logueado.
3. **Modificar cliente**: El cliente podrá modificar sus datos a través de menús dentro de las aplicaciones.
4. **Borrar cliente**:  El cliente podrá cerrar su cuenta, con lo que conllevará la eliminación total de los datos almacenados en la base de datos manualmente a través de menús dentro de la aplicación misma. Si eso fallase, se podría contactar con un administrador para la realización de tal eliminación.
5. **Añadir gasto**: El cliente podrá añadir un nuevo gasto manualmente o con la ayuda de captura fotográfica y reconocimiento de texto usando Machine Learning. Este gasto se añadirá al gasto del día, y será configurable y clasificable de varias formas, tales como categorías o etiquetas, además de la especificación de sus componentes, si procede.
6. **Modificar gasto**: El cliente podrá, si hubo algún error, modificar los valores asociados a un gasto. Por ejemplo, si tiene un gasto de 120€, y uno de los artículos que componen este gasto o factura fue almacenado con un valor incorrecto, se podrá arreglar tras su inserción.
7. **Eliminar gasto**: Si un gasto se ha añadido por error, se podría eliminar de la base de datos tras la inserción por el cliente.
8. **Crear etiquetas**: El cliente podrá crear etiquetas o categorías personalizadas para la clasificación de gastos (por ejemplo: electricidad, comida, vicios, etc).
9. **Modificar etiquetas**: El cliente podrá cambiar el nombre de las etiquetas.
10. **Eliminar etiquetas**: El cliente podrá eliminar una etiqueta, eliminándose en cascada de todos los artículos relacionados con esa etiqueta.
11. **Crear método de pago**: El cliente podrá crear métodos de pago para la clasificación de gastos (por ejemplo: tarjeta de crédito).
12. **Modificar método de pago**: El cliente podrá cambiar el nombre del método de pago.
13. **Eliminar método de pago**: El cliente podrá eliminar un método de pago, eliminándose en cascada de todos los artículos relacionados con esa etiqueta.
14. **Creación de presupuestos**: El cliente podrá crear un presupuesto mensual (o dentro de otro periodo) dentro del cual quieren mantenerse, para que la aplicación pueda responder más apropiadamente a los gastos insertados, además de reflejarse en datos estadísticos.
15. **Modificación de presupuestos**: El cliente podrá modificar presupuestos de forma que se ajusten a distintos límites y periodos en los que se aplica.
16. **Borrado de presupuestos**: El cliente podrá borrar e invalidar un presupuesto creado anteriormente.
17. **Creación de informes**: El cliente podrá configurar la creación informes estadísticos periódicos customizados o por semana, mes o año, además de la posibilidad de pedir un informe en cualquier momento manualmente. Dichos informes podrán contener variadas gráficas de acuerdo a ciertas variables que se podrían especificar, además de consejos genéricos de acuerdo a los resultados y otros factores asociados, tales como los presupuestos e información pasada.

## Tipos de usuarios/as

Esta aplicación sólo tendría un tipo de usuario/a básico, con la posibilidad en algún momento de ser "premium", con acceso a funcionalidades o preferencias extra, u otras ventajas tales como ausencia de anuncios, que requerirían algún pago.

## Normativa

Esta aplicación cumple con los requisitos asociados con la Reglamentación General de Protección de Datos (GDPR) y la Ley Orgánica 3/2018, de 5 de diciembre, de Protección de Datos Personales y garantía de los derechos digitales (LOPDPGDD).

### Aviso legal

1. Al utilizar esta aplicación, la persona usuaria de esta aplicación se asume que acepta automáticamente los términos y condiciones establecidos.
2. No existen requisitos de eligibilidad para determinar quienes pueden usar esta aplicación, incluida una edad mínima.
3. La persona usuaria debe crear, antes de poder usar la aplicación, una cuenta personal que incluya un nombre identificador único, además de un email único y una contraseña mediante un formulario proporcionado por la aplicación. En este contexto, la palabra "único" es relativa a otros clientes que ya existan antes de dicha creación de cuenta.
4. La persona usuaria de la aplicación tiene la obligación de utilizar la aplicación de forma legal, sin violar derechos de otras personas, además de la integridad de la aplicación en sí o cuentas de otros clientes.
5. No somos responsables de cualquier pérdida, daño o problema que pueda surgir a través del uso de la aplicación, a menos que sean causados por una negligencia grave.
6. Tenemos el derecho reservado a modificar, actualizar, o suspender la aplicación en cualquier momento. Los usuarios deberían revisar periódicamente los términos y condiciones para estar al tanto de cualquier cambio.
7. La cuenta de una persona usuaria puede ser terminada permanentemente si se violan las reglas aquí descritas, tales como violaciones de los derechos de otras personas, o la integridad de la aplicación.

### Política de privacidad

1. La aplicación sólo recopia datos que la persona usuaria introduce voluntariamente, además de un nombre de usuario y email, tras su consentimiento voluntario.
2. La finalidad de los datos recopilados es únicamente para proveer de utilidad a la persona usuaria y no se comparten con terceros en ningún caso.
3. Todas las personas usuarias tienen derecho total al acceso a su información, además de rectificación y eliminación, a menos que esta persona haya violado los términos de uso, en cuyo caso, estos derechos estarían sujetos a otras consideraciones.
4. La información tendrá todas las medidas seguridad actuales posibles para garantizar que los ataques cibernéticos son menos probables o efectivos, y la información sensible, tal como sería la contraseña, sería almacenada únicamente de forma encriptada con criptografía asimétrica.

### Política de cookies

1. No utilizamos cookies: nuestra aplicación no utiliza cookies para recopilar información de los usuarios.
2. La información de los usuarios es exclusivamente la proporcionada por la persona usuaria manual y voluntariamente, además de un nombre de usuario y una dirección de email.
