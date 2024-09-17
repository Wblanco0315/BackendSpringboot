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
   - **Descripción:** Permite crear una nueva cuenta de usuario proporcionando datos como cedula y contraseña,
     el primer usuario en ser creado en la bd sera admin, los demas por defecto seran client.
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
    "cedula":"",
    "password":""
}

```
### 3. 💵 **Cierre de sesion**
   - **URL:** `http://localhost:8080/auth/logout`
   - **Método:** `POST`
   - **Descripción:** Cierra la sesion y caduca el token actual.
   - **Auth Type:** Bearer Token

### 4. 💵 **Consultar Saldo**
   - **URL:** `http://localhost:8080/transaccion/balance`
   - **Método:** `GET`
   - **Descripción:** Obtiene el saldo de usuario.
   - **Auth Type:** Bearer Token
     
### 5. 🏧 **Retiro de Fondos**
   - **URL:** `http://localhost:8080/transaccion/withdrawal`
   - **Método:** `POST`
   - **Descripción:** Permite retirar una cantidad específica de dinero de la cuenta del usuario a travez de la cedula.
   - **Auth Type:** Bearer Token
```
{
    "identifier":"",
    "amount":
}
```
### 6. 💳 **Depósito de Fondos**
   - **URL:** `http://localhost:8080/transaccion/deposit`
   - **Método:** `POST`
   - **Descripción:** Permite depositar una cantidad de dinero en la cuenta del usuario.
   - **Auth Type:** Bearer Token
```
{
    "identifier":"",
    "amount":
}
```
### 7. 🔄 **Transferencia entre Usuarios**
   - **URL:** `http://localhost:8080/transaccion/transfer`
   - **Método:** `POST`
   - **Descripción:** Realiza una transferencia de fondos desde la cuenta del usuario autenticado hacia la cuenta de otro usuario digitado con la cedula.
   - **Auth Type:** Bearer Token
```
{
    "identifierOrigen":"",
    "identifierDestino":"",
    "amount":
}
```
### 8. 📋 **Lista de Depositos del usuario**
   - **URL:** `http://localhost:8080/tables/deposits`
   - **Método:** `GET`
   - **Descripción:** Muestra una lista de depositos hechos por el usuario.
   - **Auth Type:** Bearer Token
     
### 9. 💰 **Lista de Retiros del Usuario**
   - **URL:** `http://localhost:8080/tables/withdrawals`
   - **Método:** `GET`
   - **Descripción:** Muestra una lista de retiros hechos por el usuario.
   - **Auth Type:** Bearer Token

### 10. 🧾 **Historial de Todas las Transacciones del Usuario**
   - **URL:** `http://localhost:8080/tables/transactions`
   - **Método:** `GET`
   - **Descripción:** Muestra el historial de transacciones realizadas por el usuario autenticado.
   - **Auth Type:** Bearer Token

### 11. 👥 **Lista de Usuarios Registrados en el Sistema**
   - **URL:** `http://localhost:8080/admin/users`
   - **Método:** `GET`
   - **Descripción:** Muestra la lista de usuarios registrados en el sistema. Requiere rol admin.
   - **Auth Type:** Bearer Token

### 12. 🕵️‍♂️ **Lista de Auditorías**
   - **URL:** `http://localhost:8080/admin/audits`
   - **Método:** `GET`
   - **Descripción:** Muestra todas las transacciones realizadas en todos sus estados. Requiere rol admin.
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
