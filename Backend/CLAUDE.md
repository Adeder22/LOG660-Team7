# TP2-LOG660 — Webflix (LOG660 Lab 2, ÉTS)

Three-tier film-rental app on Oracle, persistence through Hibernate (ORM). Team EQUIPE107.

- Lab brief: `C:\Users\samue\OneDrive\Desktop\A2026\LOG660-01\TP2\LOG660-Labo2-Enonce.pdf`
- Use cases (Webflix): `C:\Users\samue\OneDrive\Desktop\A2026\LOG660-01\TP1\LOG660-MiseEnSituation-revise.pdf`
- Scope: **only use cases 2 (connexion client), 3 (consultation de films), 4 (location de films).**

## Stack
- Java 8, Maven, Hibernate 4.3.1 (annotations, `javax.persistence`), Oracle (`ojdbc6`), `Oracle10gDialect`
- DB: `bdlog660.ens.ad.etsmtl.ca:1521/ORCLPDB1`, schema `EQUIPE107` (ÉTS network / VPN required)
- `hbm2ddl.auto=validate`: entity mappings must match the existing tables exactly

## Architecture rules (graded)
- Presentation → `FacadeApplication` (façade pattern) → DAO → Hibernate. Presentation never touches DAOs or entities directly.
- Queries in **HQL on entities**, not native SQL (ORM principle). Always bind values with named parameters (`:param`).
- One `Session` per operation, closed in `finally`. Writes go inside `beginTransaction()` / `commit()`.
- DTOs (`com.log660.dto`) are returned to the presentation layer via `select new ...`.
- Artificial keys should use `native` generation (brief requirement).
- Field names in French, without accents (e.g. `nomRealisateur`, not `nomRéalisateur`). Classes, methods and variables
  in **English** (user preference, e.g. `SearchCriteria`, `FilmDAO.search`, `FacadeApplication.login`). Getters/setters follow the French field names (`getTitre`).
  Entity classes (generated) and HQL parameter names (`:courriel`, `:titre0`) stay as they are.
  All names must be readable and meaningful (readability is graded).

## Project layout
- `com.log660.models`: JPA entities (Usager, Client, Employe, Adresse, Cartecredit, Forfait, Film, Personne, Genre, PaysProduction, Copie, Pret, BandesAnnonce, Film* join entities)
- `com.log660.dao`: `ClientDAO`, `FilmDAO`
- `com.log660.dto`: `ClientInfo`, `FilmInfo`, `SearchCriteria`
- `com.log660.facade`: `FacadeApplication`
- `org.example`: `HibernateUtil` (single SessionFactory), `Main` (console test entry point)
- `src/main/resources/hibernate.cfg.xml`: connection, `default_schema=EQUIPE107`, entity mappings

## Checkpoint (2026-10-14, end of day: film search done + English renaming)

### ⚠ REMINDER FOR NEXT SESSION (user request)
- **The user wants an explanation of all the work Claude did on 2026-10-14** before going further: walk through each file and
  the reasoning, so the user can verify and understand it. Files touched today: `dto/FilmInfo.java` (getters, `Short` types),
  `dto/SearchCriteria.java` (new), `dao/FilmDAO.java` (`search`, `addContent`, `isNotEmpty`), `dao/ClientDAO.java` and
  `facade/FacadeApplication.java` (English renaming + `searchFilms`), `org/example/Main.java` (renamed calls), `CLAUDE.md`.
  Topics to cover: dynamic HQL with `where 1 = 1` (a list + `String.join` version was tried and reverted by the user for readability),
  word split for "pas nécessairement en ordre", `exists` subqueries vs joins, repeated criteria with indexed aliases/parameters,
  named parameters and SQL injection, `left join` on the director, `select new` constructor types (`Short` for `NUMBER(4,0)`).

### Done
- Hibernate config + entity mappings for all tables; connection verified.
- **Use case 2: login.** `FacadeApplication.login(email, password)` → `ClientDAO.authenticate` → HQL
  `select new ClientInfo(u.id, u.nom, u.prenom) from Client c join c.usager u where u.courriel = :courriel and u.motdepasse = :motdepasse`.
  Clients only (employees excluded by joining from `Client`). Email is case-sensitive and trimmed; password stored in plain text.
  Returns `null` on failure; one generic error message ("Courriel inconnu ou mot de passe incorrect. Veuillez entrer de nouvelles valeurs.").
  Confirmed by a run of `Main`: "Connexion réussie : Robert Flores (no usager 997690)".
- **Use case 3, film search (DAO + façade).** `FacadeApplication.searchFilms(SearchCriteria)` → `FilmDAO.search` → `List<FilmInfo>` sorted by title.
  - `SearchCriteria` (dto): `titre`, `anneeMin`/`anneeMax` (`Short`), `langue`, `realisateur` (setters) + repeatable lists
    `acteurs`, `genres`, `pays` (`addActor`, `addGenre`, `addCountry`). Empty/null criteria are ignored.
  - Dynamic HQL: `from Film f left join f.norealisateur r where 1 = 1` + one condition per criterion, values in a `Map` bound by named parameters.
  - "pas nécessairement en ordre": title, director and actor text is split into words, one `lower(...) like :pN` per word (`addContent`).
  - Genres / countries / actors: one `exists` subquery per value (indexed aliases `fg0`, `fp0`, `fa0`…); all words of one actor stay in the same `exists`.
  - All text comparisons case-insensitive. Verified against the DB (631 films without criteria; `king lion` → The Lion King; Hanks + Ryan → 2 films).
  - `FilmInfo`: noFilm, titre, anneeSortie (`Short`), langueOriginale, dureeMinutes (`Short`), nomRealisateur, resumeScenario, affiche.
  - SQL injection: safe (only fixed fragments concatenated, all user values bound). `%`/`_` typed by the user act as `like` wildcards (accepted, mention in report).
  - No input validation in DTO/DAO/façade by user's choice (see Servlet note below).
  - `Main` tests the search (`king lion`, 1990–2000, Animation → The Lion King (1994)).
    It checks `films == null`, which never happens (`search` returns an empty list); use `films.isEmpty()` for the "no film found" message.
  - Names: classes/methods/variables renamed to English today (`SearchCriteria`, `search`, `searchFilms`, `login`, `authenticate`,
    `addActor/addGenre/addCountry`, `addContent`, `isNotEmpty`). Javadoc comments and HQL parameter names are still in French.

### In progress
- **Use case 3, film sheet.** Not started. Needs `details(noFilm)` + a richer DTO: title, year, all countries, language, duration, all genres, director,
  all writers, all actors + characters, summary, poster, all trailer links. Clicking director/actor → name, birth date, birth place, photo, biography.
  `Film` has no collections; query via the join entities (`FilmGenre`, `FilmActeur`, `FilmPay`, `FilmScenariste`, `BandesAnnonce`).
- Use case 3 presentation (later, in the thin client): format results as `Titre (année)`; error message when the list is empty.

### To do
- **Use case 4: rental.** Rent from the film sheet only if a copy is available AND the client's current rentals < `Forfait.locationmax`
  (D=1/10 days, I=5/30 days, A=10/unlimited). Distinct meaningful error messages. Inside a transaction.
- **Rework `COPIE` / `PRET` schema** (planned by user): no way today to tell an available copy from a rented one
  (`Copie` has no status, `Pret.dateretour` is NOT NULL). Fix before use case 4.
- **Key generation:** `Copie` and `Pret` use `GenerationType.IDENTITY`, which `Oracle10gDialect` doesn't support; switch to native (`AUTO` + sequence).
- **Thin client (Task 3):** servlet web app (course skeleton + "Application Web et Servlet Java" guide) on Tomcat, or a Swing GUI.
  Store `ClientInfo` in `HttpSession` after login. Needs `pom.xml` changes (`war` packaging, Servlet API dependency).
  - **REMINDER (user request): graceful type check in the Servlet.** Form fields arrive as `String`; convert them to the right type when possible
    (e.g. `anneeMin`/`anneeMax` → `Short`, trim spaces) without crashing on bad input (`NumberFormatException` caught). Do it in the servlet layer,
    not in DTO/DAO/façade.
- **`pom.xml` cleanup:** `marven.compiler.*` typo (should be `maven.compiler.*`); stray `<property name="hibernate.dialect">` inside `<properties>`.
  Maven isn't on PATH (IntelliJ's bundled one is at `C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.3/plugins/maven-plugin/lib/maven3/bin/mvn`).
- **Report (Task 4):** fill the course template, justify design decisions (repeatable criteria only for multi-valued ones, `exists` vs join, word split, injection protection).
- Submission: `log660-labo2-equipe107` archive with report, config/mapping files and source code.
