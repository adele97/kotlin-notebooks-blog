# Kotlin Notebooks Blog

Example repo for a Kotlin Notebooks project that uses Postgres as the data source.

## What you need

1. IntelliJ Ultimate, including the [Kotlin Notebooks plug-in](https://plugins.jetbrains.com/plugin/16340-kotlin-notebook)
2. Kotlin, Gradle and a JDK
3. Docker

## Getting started

1. Clone the repo into IntelliJ and build the project
2. Run `docker compose up` from the root directory (or press the arrow next to "services" in the `docker-compose.yml` file). This will create a postgres datasource including the `powerlifting_data` table
3. Run the `main` function in `src/main/kotlin/data/DataImporter.kt`. This will populate your database with the data from the `openpowerlifting2023.csv` file in the project root