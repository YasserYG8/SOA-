# 🚀 CarHub REST API Testing Cheat Sheet

> **Cars API:** `http://localhost:8082` (database `cars_db`)
> **Users service:** `http://localhost:8081` (database `users_db`)
> **Headers:** `Content-Type: application/json` and `Authorization: Bearer <token>`

## 🔐 Get a token first

Every Cars API call needs a token. Send this request, then copy the `Authorization` response header:

```bash
curl -i -X POST http://localhost:8081/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

| Account | Password | Roles |
|:-------:|:--------:|:-----:|
| `admin` | `admin123` | ADMIN, USER: can read and write |
| `yasser` | `yasser123` | USER: can only read |

| Situation | Status |
|:---------:|:------:|
| No token, invalid token or expired token | `401` |
| Valid token, but USER tries to POST, PUT or DELETE | `403` |

In Postman, the collection saves the token for you: run **Login** first.

## 📝 Register a new account

1. `POST http://localhost:8081/register` with `{"username":"driver1","email":"driver1@example.com","password":"secret123"}`
2. Find the 6-digit code in your email. Without `MAIL_USERNAME` set, it is in the `users_proj` console.
3. `GET http://localhost:8081/verifyEmail/{code}`
4. Log in with the new account.

---

## 📋 Endpoint Reference Table

| # | HTTP Method | Endpoint Path | Description | Expected Status |
|:-:|:-----------:|:--------------|:------------|:---------------:|
| 1 | `GET` | `/api/all` | Get all cars in the showroom fleet | `200 OK` |
| 2 | `GET` | `/api/{id}` | Get car details by ID (e.g. ID `1`) | `200 OK` |
| 3 | `GET` | `/api/brands` | Get all automotive manufacturers | `200 OK` |
| 4 | `GET` | `/api/brands/{id}` | Get brand details by ID (e.g. ID `1`) | `200 OK` |
| 5 | `GET` | `/api/carsbrand/{idBrand}` | Filter cars by brand ID (e.g. BMW = `1`) | `200 OK` |
| 6 | `GET` | `/api/carsbymodel/{model}` | Search cars by model keyword (e.g. `M4`) | `200 OK` |
| 7 | `POST` | `/api` | Create / Register a new car | `200 OK` |
| 8 | `PUT` | `/api` | Update an existing car | `200 OK` |
| 9 | `DELETE` | `/api/{id}` | Delete a car by ID | `200 OK` |

---

## 🧪 Detailed Tests for Postman & cURL

### 1. Get All Cars (Showroom Fleet)
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/all`

---

### 2. Get Single Car by ID
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/1`

---

### 3. Get All Brands
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/brands`

---

### 4. Get Single Brand by ID
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/brands/1`

---

### 5. Filter Cars by Brand ID
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/carsbrand/1` *(1: BMW, 2: Audi, 3: Mercedes, 4: Porsche, 5: Ferrari)*

---

### 6. Search Cars by Model Name
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/carsbymodel/M4`

---

### 7. Create / Add a New Car
* **Method:** `POST`
* **URL:** `http://localhost:8082/api`
* **Headers:** `Content-Type: application/json`
* **Body (raw JSON):**
```json
{
  "model": "Roma Spider",
  "price": 245000.0,
  "releaseDate": "2024-05-10",
  "licensePlate": "249-TUN-8888",
  "imagePath": "https://images.unsplash.com/photo-1592198084033-aade902d1aae?auto=format&fit=crop&w=800&q=80",
  "brand": {
    "idBrand": 5
  }
}
```

---

### 8. Update an Existing Car
* **Method:** `PUT`
* **URL:** `http://localhost:8082/api`
* **Headers:** `Content-Type: application/json`
* **Body (raw JSON):**
```json
{
  "idCar": 1,
  "model": "M4 Competition xDrive",
  "price": 94500.0,
  "releaseDate": "2023-04-12",
  "licensePlate": "235-TUN-4040",
  "imagePath": "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=800&q=80",
  "brand": {
    "idBrand": 1
  }
}
```

---

### 9. Delete a Car by ID
* **Method:** `DELETE`
* **URL:** `http://localhost:8082/api/{id}` *(e.g. `http://localhost:8082/api/8`)*
