# Funcionalidad de Perfil de Usuario - Actualizada

## Descripción
Se ha implementado la funcionalidad completa para que los usuarios puedan actualizar su perfil en la aplicación, respetando todas las restricciones y validaciones del registro.

## Características Implementadas

### 1. Actualización de Datos del Perfil
- **Campos editables**: Nombre, Apellidos, Correo, Domicilio, Lada, Teléfono
- **Campos no editables**: Fecha de nacimiento, Edad, Rol, Matrícula, Contraseña
- **Validaciones**: Todos los campos son obligatorios

### 2. Validaciones Implementadas

#### Correo Institucional
- Debe terminar con `@utez.edu.mx`
- Solo permite caracteres válidos (letras, números, puntos, guiones bajos)
- No permite espacios
- Debe tener al menos un carácter antes del @

#### Teléfono
- Solo números permitidos
- Longitud entre 7 y 10 dígitos
- Se eliminan espacios automáticamente

#### Lada
- Solo números permitidos
- Longitud entre 2 y 4 dígitos
- Se eliminan espacios automáticamente

### 3. Cambio de Contraseña
- **Validación de contraseña actual**: Verifica que el usuario conozca su contraseña actual
- **Validación de nueva contraseña**: Aplica las mismas reglas de seguridad del registro
- **Confirmación**: Requiere que el usuario confirme la nueva contraseña
- **Reglas de seguridad**:
  - Mínimo 8 caracteres
  - Al menos una letra
  - Al menos un número
  - Al menos un carácter especial

### 4. Funcionalidades de la Interfaz

#### Botones de Acción
- **Guardar Cambios**: Valida y guarda los cambios en la base de datos
- **Cancelar**: Restaura los valores originales del perfil
- **Cambiar Contraseña**: Abre un diálogo para cambiar la contraseña

#### Gestión de Foto de Perfil
- **Cambiar foto**: Permite seleccionar una nueva imagen
- **Eliminar foto**: Restaura la imagen por defecto
- **Formatos soportados**: PNG, JPG, JPEG

### 5. Persistencia de Datos

#### Base de Datos
- **Método `updatePerfil`**: Actualiza solo los campos del perfil (sin afectar datos sensibles)
- **Transacciones seguras**: Uso de PreparedStatement para prevenir inyección SQL
- **Manejo de errores**: Captura y maneja excepciones de base de datos

#### Sesión de Usuario
- **Actualización automática**: Los cambios se reflejan inmediatamente en la sesión
- **Consistencia**: Mantiene sincronizados los datos de la interfaz y la base de datos

### 6. Manejo de Errores

#### Validaciones en Tiempo Real
- **Campos vacíos**: Muestra alerta de error
- **Formato inválido**: Mensajes específicos para cada tipo de error
- **Errores de base de datos**: Captura y muestra errores de conexión

#### Alertas Informativas
- **Éxito**: Confirma que los cambios se guardaron correctamente
- **Error**: Informa sobre problemas específicos
- **Información**: Notifica sobre acciones como cancelación de cambios

## Estructura de Archivos Modificados

### 1. `VistaPerfil.java`
- Implementación completa de la interfaz de perfil
- Validaciones de campos
- Lógica de guardado y cancelación
- Funcionalidad de cambio de contraseña

### 2. `UsuarioDaoImpl.java`
- Nuevo método `updatePerfil()` para actualizar solo datos del perfil
- Corrección del método `create()` (cambiado de `executeQuery` a `executeUpdate`)

### 3. `IUsuarioDao.java`
- Nueva interfaz `updatePerfil()` para operaciones específicas del perfil

### 4. `ControladorPerfil.java`
- Controlador actualizado para manejar la nueva funcionalidad

## Flujo de Uso

1. **Acceso al Perfil**: El usuario navega a la vista de perfil
2. **Edición de Campos**: Modifica los campos deseados
3. **Validación**: El sistema valida todos los campos antes de guardar
4. **Guardado**: Si las validaciones pasan, se actualiza la base de datos
5. **Confirmación**: Se muestra mensaje de éxito y se actualiza la sesión

## Consideraciones de Seguridad

- **Validación de entrada**: Todos los campos se validan antes de procesar
- **Prevención de inyección SQL**: Uso de PreparedStatement
- **Verificación de contraseña**: Requiere contraseña actual para cambios
- **Restricción de campos**: Solo permite editar campos no sensibles del perfil

## Compatibilidad

- **JavaFX**: Compatible con la versión utilizada en el proyecto
- **Base de Datos**: Compatible con Oracle (uso de TO_DATE)
- **Java**: Compatible con Java 8+

## Próximas Mejoras Sugeridas

1. **Validación en tiempo real**: Mostrar errores mientras el usuario escribe
2. **Historial de cambios**: Registrar cambios realizados en el perfil
3. **Respaldo automático**: Crear respaldo antes de aplicar cambios
4. **Notificaciones**: Enviar confirmación por correo de cambios realizados
5. **Auditoría**: Registrar quién y cuándo realizó cambios en el perfil

