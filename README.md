# 💸 **Sistema de Transacciones**

Este proyecto es un sistema de gestión de transacciones financieras que permite a los usuarios realizar diversas operaciones como registro, login, consulta de saldo, retiros, depósitos, transferencias y auditoría de transacciones.

## 🌟 **Características**

- 📝 Registro de nuevos usuarios.
- 🔐 Inicio de sesión para autenticación.
- 🔙 Cierre de sesión
- 🏦 Operaciones de retiro, depósito y transferencia de fondos.
- 📊 Auditoría y visualización del historial de todos los tipos de transacciones.

---

## 🔗 **Rutas Disponibles**

### 1. 📝 **Registro de Usuario**
   - **URL:** `http://localhost:8080/auth/register`
   - **Método:** `POST`
   - **Descripción:** Permite crear una nueva cuenta de usuario proporcionando datos como cedula y contraseña.
```
{
    "name":"",
    "cedula":"",
    "email":"",
    "password":""
}
```
### 2. 🔐 **Inicio de Sesión**
   - **URL:** `http://localhost:8080/auth/login`
   - **Método:** `POST`
   - **Descripción:** Inicia sesión en el sistema utilizando credenciales válidas. Devuelve un token JWT para autenticación.
   - **Admin por defecto :**
```
{
    "cedula":"0123456789",
    "password":"admin123"
}

```
### 3. 💵 **Cierre de sesion**
   - **URL:** `http://localhost:8080/transaccion/balance`
   - **Método:** `GET`
   - **Descripción:** Muestra el saldo disponible en la cuenta del usuario autenticado.
   - **Auth Type:** Bearer Token

### 4. 🏧 **Retiro de Fondos**
   - **URL:** `http://localhost:8080/transaccion/retiro`
   - **Método:** `POST`
   - **Descripción:** Permite retirar una cantidad específica de dinero de la cuenta del usuario.
```
{
    "monto":"1000000.00",
    "cedula":"1065863389",
    "password":"12345678"
}
```
### 5. 💳 **Depósito de Fondos**
   - **URL:** `http://localhost:8080/transaccion/depositar`
   - **Método:** `POST`
   - **Descripción:** Permite depositar una cantidad de dinero en la cuenta del usuario.
```
{
    "monto":"100000",
    "cedula":"1065863389"
}
```
### 6. 🔄 **Transferencia entre Usuarios**
   - **URL:** `http://localhost:8080/transaccion/transferencia`
   - **Método:** `POST`
   - **Descripción:** Realiza una transferencia de fondos desde la cuenta del usuario autenticado hacia la cuenta de otro usuario digitado.
   - **Auth Type:** Bearer Token
```
{
    "monto":3000.00,
    "cedula":"0123456789"
}
```
### 7. 📋 **Auditoría de Transacciones (Todos los Usuarios)**
   - **URL:** `http://localhost:8080/transaccion/auditoria`
   - **Método:** `GET`
   - **Descripción:** Muestra la tabla completa de todas las transacciones realizadas por todos los usuarios del sistema (requiere permisos de administrador).
   - **Auth Type:** Bearer Token

### 8. 📜 **Historial de Transacciones del Usuario**
   - **URL:** `http://localhost:8080/transaccion/historial`
   - **Método:** `GET`
   - **Descripción:** Muestra el historial de transacciones realizadas por el usuario autenticado.
   - **Auth Type:** Bearer Token
---

## 🛠️ **Tecnologías Utilizadas**

- **Java Spring Boot** ☕
- **Maven** 📦
- **MySQL** 🗄️

### 📦 **Dependencias Principales**

- **jakarta validation api** ✅
- **jackson-datatype**💾
- **hibernate validator** ✔️
- **JWT** 🔑 (para la autenticación)
- **JPA** 🗃️
- **spring security** 🔐
- **log4j2** 📝
- **lombok** 📕

---

## 🚀 **Instrucciones de Ejecución**

1. Clona el repositorio.
2. Ve a ..\transacciones-logs\src\main\resources en el archivo aplication.properties.
3. Utiliza tus credenciales para configurar tu base de datos postgres y guarda.
4. Ejecuta.
5. Accede a las rutas proporcionadas para realizar las operaciones.
