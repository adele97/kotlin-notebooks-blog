package util

import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.io.db.PostgreSql
import org.jetbrains.kotlinx.dataframe.io.readResultSet

const val URL = "jdbc:postgresql://localhost:5432/demo"
const val USER_NAME = "demo"
const val PASSWORD = "build"

class Helpers {

    fun fetchResults(query: String): AnyFrame {
        val connection: Connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)

        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        val df = DataFrame.readResultSet(resultSet, PostgreSql)

        resultSet.close()
        statement.close()

        connection.close()

        return df
    }
}