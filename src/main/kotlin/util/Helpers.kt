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

///#%% md
//Since we will be fetching data often, and the function does not change a lot, we can use the self-defined `fetchResults` function from the `Helpers` class in the `util` package to import the data into the notebook as dataframe. This keeps the boilerplate out of our notebook. If the `Helpers` class didn't import to the notebook, make sure you go to the settings for this notebook and `select modules to use in the notebook`