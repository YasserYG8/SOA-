# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

CarHub is a course mini-project: a car showroom secured with JWT. It has three independent builds and no root build:

| Folder | What it is | Port | Database |
| --- | --- | --- | --- |
| `cars_proj/` | Spring Boot 3.3 / Java 21 API for cars and brands | 8082 | `cars_db` |
| `users_proj/` | Spring Boot 3.3 / Java 21 service for login and registration | 8081 | `users_db` |
| `cars-frontend/` | Angular 20 / Tailwind 3 app | 4200 | none |

Both databases live in the same Docker MySQL 8.4 and are created on first start. phpMyAdmin takes port 8080.

## Local settings

`JWT_SECRET`, `MAIL_USERNAME` and `MAIL_PASSWORD` live in a `.env` file at the repo root, which Git ignores (`.env.example` is the template). Both services import it through `spring.config.import` in `application.properties` and need a restart after a change. It is read as a properties file, so values take no quotes. Read settings through Spring properties, because `System.getenv` does not see this file.

## Commands

Start the database first. Both backends and their tests need it.

```bash
docker compose up -d        # from the repo root: MySQL on 3306, phpMyAdmin on http://localhost:8080
```

Each backend, from its own folder (`sh mvnw` on Linux/Mac, since `mvnw` is not marked executable):

```bash
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test                                                   # all tests
.\mvnw.cmd test -Dtest=SecurityRulesTests#userCannotWrite         # a single test (cars_proj)
```

Frontend, from `cars-frontend/`:

```bash
npm start       # ng serve on http://localhost:4200
npm run build   # ng build
npm test        # ng test (Karma/Jasmine), but no *.spec.ts files exist yet
```

No linter is configured. Prettier settings live in `cars-frontend/package.json`.

To exercise the APIs by hand, use `CarHub_Postman_Collection.json` (run its Login request first) or `API_TESTING_SHEET.md`. Demo accounts: `admin` / `admin123` (ADMIN, USER) and `yasser` / `yasser123` (USER).

## Architecture

**Authentication flow:** `users_proj` creates tokens and `cars_proj` only checks them. `POST /login` is handled by `JWTAuthenticationFilter`, which returns the token, without a `Bearer ` prefix, in the `Authorization` response header. Angular stores it in `localStorage`, and `token.interceptor.ts` adds `Bearer <token>` to every request except `/login`, `/register` and `/verifyEmail`. Each backend's `JWTAuthorizationFilter` verifies the token. Both read the key from the `jwt.secret` property, which comes from `JWT_SECRET` with a shared development fallback, so **the two services must use the same value**. `SecParams` and the filter are deliberately duplicated in each project.

**Security rules:** `cars_proj/.../security/SecurityConfig.java` holds the URL rules: GET needs ADMIN or USER, and writes need ADMIN. `@PreAuthorize` on the controller's write methods repeats the check. A new endpoint falls through to `anyRequest().authenticated()`, so any logged-in user can reach it until a rule is added. An invalid token leaves the caller anonymous rather than raising an error, so public routes keep working with a stale token and protected routes answer 401. CORS allows only `http://localhost:4200` and is configured in `SecurityConfig`, not with `@CrossOrigin`.

**Backends** follow `restcontrollers` → `service` → `repos` → `entities`. Entities use handwritten getters and setters. Lombok is a dependency of `cars_proj` only and is unused.

**Brands have two APIs in `cars_proj`:** the controller's `/api/brands` returns plain JSON, and Spring Data REST's `/brand` returns HAL. Angular uses `/brand`, so lists arrive as `b._embedded.brands` (`BrandWrapper`). `RestConfig` exposes `idBrand`, which Spring Data REST hides by default. `spring.data.rest.detection-strategy=annotated` keeps `CarRepository` unpublished; removing it would create a `/cars` endpoint.

**Frontend** uses standalone components routed in `app.routes.ts`, with `authGuard` (logged in) and `carGuard` (ADMIN). URLs come from `src/environments/`. Component files have no `.component` suffix (`cars/cars.ts` exports `CarsComponent`). The fleet page filters and sorts client-side in `CarsComponent.applyFiltersAndSorting()`; the search pages demonstrate the server, keyup and pipe techniques.

**API contract:** `Brand.cars` is `@JsonIgnore`d, so a `Car` serializes with its nested brand and a `Brand` never lists its cars. POST and PUT bodies reference a brand by nested object: `"brand": { "idBrand": 5 }`. Missing records return `200` with an empty body rather than `404`, because the service uses `findById(...).orElse(null)`. `users_proj` errors are JSON: `{ errorCode, message }`.

## Registration

`POST /register` saves the user with `enabled = false` and a 6-digit `VerificationToken` valid for 15 minutes, then emails the code. `GET /verifyEmail/{code}` enables the account. Registering again with the username or email of an unverified account replaces it, which is how a user gets a new code. A failed email send rolls the registration back.

While `MAIL_USERNAME` is unset, `EmailService` prints the code in the `users_proj` console instead of sending it.

## Database initialization (`cars_proj`)

Several mechanisms overlap, and the order matters. With `spring.jpa.defer-datasource-initialization=true`, each startup runs:

1. Hibernate `ddl-auto=update`
2. `schema.sql`
3. `data.sql`, because `spring.sql.init.mode=always`
4. The `CommandLineRunner` seeder in `CarsProjApplication`

Consequences:

- `data.sql` upserts brands 1–5 and cars 1–7 with `ON DUPLICATE KEY UPDATE`, so **any edit or delete of those rows is undone on the next restart**. Use a new row when testing persistence across restarts.
- The `CommandLineRunner` only seeds when no brands exist, so after `data.sql` it does nothing. Change seed data in `data.sql`.
- `cars_db.sql` at the repo root is a manual script that **drops and recreates** both tables. Nothing runs it automatically.
- A schema change must be made in the entity, `schema.sql` and `cars_db.sql` to stay consistent.

`users_proj` relies on Hibernate `ddl-auto=update` alone and seeds its roles and demo accounts in `UsersProjApplication`.

## Tests

All tests are `@SpringBootTest` against the real MySQL databases, with no test profile or in-memory database. To keep your data clean, point them at a throwaway database by setting `SPRING_DATASOURCE_URL`.

- `cars_proj/SecurityRulesTests` and `users_proj/RegistrationTests` assert behaviour and roll back with `@Transactional`.
- `users_proj/UsersProjApplicationTests` asserts login and role rules, and only reads.
- `cars_proj/CarsProjApplicationTests` prints results rather than asserting, so a pass only shows the queries executed. It also changes data: each run of `testCreateCar` adds another Ferrari brand and `296 GTB` car, and `testUpdateCar` raises the first car's price.

## Conventions

The course brief requires domain-specific names that differ from the course's own: `idCar`, `model`, `price`, `releaseDate`, `licensePlate` (not `nomProduit`, `prixProduit`, `dateCreation`), and `cars`/`brands` for variables (not `prods`/`cats`). The UI must stay on Tailwind CSS, not Bootstrap.
