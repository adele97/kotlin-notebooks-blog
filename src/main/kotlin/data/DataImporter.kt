package data

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.time.format.DateTimeParseException
import java.time.LocalDate

fun main() {

    // Database connection details
    val jdbcUrl = "jdbc:postgresql://localhost:5432/demo"
    val username = "demo"
    val password = "build"

    val csvFilePath = "openpowerlifting2023.csv"
    val tableName = "powerlifting_data"

    // Establish a connection to the database
    val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)

    try {
        // Read the CSV file
        val csvFile = File(csvFilePath)
        val lines = csvFile.readLines()

        // Extract headers (assumes the first row contains column names)
        val headers = lines.first().split(",")

        // Create an insert query dynamically based on the column names
        val query = buildString {
            append("INSERT INTO $tableName (${headers.joinToString(",")}) VALUES (")
            append(headers.joinToString(",") { "?" })
            append(")")
        }

        println("Generated SQL query: $query")

        // Prepare the insert statement
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)

        // Process each row in the CSV (excluding the header)
        lines.drop(1).forEach { line ->
            val values = line.split(",")

            // Set the values for the query
            values.forEachIndexed { index, value ->
                preparedStatement.setObject(index + 1, parseValue(value))
            }

            // Execute the query
            preparedStatement.addBatch()
        }

        // Execute the batch
        preparedStatement.executeBatch()
        println("CSV data has been successfully imported into the database!")

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection.close()
        println("Database connection closed.")
    }
}


// Helper function to parse values based on their type
fun parseValue(value: String): Any? {
    return when {
        value.isBlank() -> null
        value.toDoubleOrNull() != null -> value.toDouble()
        value.equals("yes", ignoreCase = true) -> true
        value.equals("no", ignoreCase = true) -> false
        isValidDate(value) -> LocalDate.parse(value) // Check and parse date
        else -> value
    }
}

fun isValidDate(value: String): Boolean {
    return try {
        LocalDate.parse(value) // Attempt to parse the string as a date
        true
    } catch (e: DateTimeParseException) {
        false
    }
}
