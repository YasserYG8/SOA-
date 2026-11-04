# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

CarHub is a course mini-project: a car showroom CRUD app with a Spring Boot 3.3 / Java 21 backend (`cars_proj/`), an Angular 20 / Tailwind 3 frontend (`cars-frontend/`), and MySQL 8.4 in Docker. The two apps are independent builds; there is no root build.

## Commands

Start the database first. The backend and its tests both need it.

```bash
docker compose up -d        # from the repo root: MySQL on 3306, phpMyAdmin on http://localhost:8080
```

Backend, from `cars_proj/` (use `./mvnw` on Linux/Mac):

```bash
.\mvnw.cmd spring-boot:run                                        # API on http://localhost:8082
.\mvnw.cmd test                                                   # all tests
.\mvnw.cmd test -Dtest=CarsProjApplicationTests#testFindByModel   # a single test
```

Frontend, from `cars-frontend/`:

```bash
npm start       # ng serve on http://localhost:4200
npm run build   # ng build
npm test        # ng test (Karma/Jasmine), but no *.spec.ts files exist yet
```

No linter is configured. Prettier settings live in `cars-frontend/package.json`.

To exercise the API by hand, use `CarHub_Postman_Collection.json` or `API_TESTING_SHEET.md`.

## Architecture

**Backend** follows `restcontrollers` → `service` → `repos` → `entities` under `com.example.cars`. A single controller, `CarRESTController`, serves both cars and brands under `/api` with `@CrossOrigin("*")`. Entities use handwritten getters and setters even though Lombok is a dependency.

**Frontend** uses standalone components (`cars`, `add-car`, `update-car`) routed in `app.routes.ts`. All HTTP goes through `services/car.service.ts`, which hardcodes `http://localhost:8082/api`; there is no proxy or environment file. Component files have no `.component` suffix (`cars/cars.ts` exports `CarsComponent`).

The showroom's filtering, search and sorting run client-side in `CarsComponent.applyFiltersAndSorting()` over the full list from `/api/all`. The service's `filterCarsByBrand` and `searchCarsByModel` methods and their backend endpoints are not called by any component.

**API contract:** `Brand.cars` is `@JsonIgnore`d, so a `Car` serializes with its nested brand and a `Brand` never lists its cars. POST and PUT bodies reference a brand by nested object: `"brand": { "idBrand": 5 }`. Missing records return `200` with an empty body rather than `404`, because the service uses `findById(...).orElse(null)`.

## Database initialization

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

## Tests

`CarsProjApplicationTests` is a `@SpringBootTest` that runs against the real `cars_db` database, with no test profile or in-memory database. The tests print results rather than asserting, so a pass only shows the queries executed. They also change data: each run of `testCreateCar` adds another Ferrari brand and `296 GTB` car, and `testUpdateCar` raises the first car's price.

## Conventions

The course brief requires domain-specific names that differ from the course's own: `idCar`, `model`, `price`, `releaseDate`, `licensePlate` (not `nomProduit`, `prixProduit`, `dateCreation`), and `cars`/`brands` for variables (not `prods`/`cats`). The UI must stay on Tailwind CSS.

## Stale documentation

The database is `cars_db`, as set in `application.properties` and `docker-compose.yml`. Mentions of `mydatabase`, including in `README.md`'s spec table, predate a rename. The README's endpoint table also omits `GET /api/brands/{id}`.
