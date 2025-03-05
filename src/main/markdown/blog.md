# My First Toe Dip into Data Science with PostgreSQL, Kotlin Notebooks, Dataframe and Kandy

Have you ever wanted to play around with a data set, but the thought of setting up a python development environment seemed like too much of a hassle?

Or was that only me?

In my work, I sit firmly in the Kotlin/JVM ecosystem. And I like it here. I also have a hobby that I am really passionate about: powerlifting. The sport where we wear funny little jumpsuits and try to lift as much as we can for one repetition in the squat, bench press and deadlift.

Powerlifting is somewhat of a niche sport, however it is growing rapidly and has a strong, supportive community. It is also a data-driven sport which has birthed an open source data set, containing the results of powerlifting competitions held all over the world. The full dataset is over 3.3 million rows and counting. I was really interested to see what insights lay inside, but I had never written a line of python before, and didnt feel like dumping 3.3 million rows in excel. It’s also not a very elegant solution for a software engineer.

Wouldn’t it be great to use familiar language and IDE while I learn data science?

Enter Kotlin Notebooks.

Kotlin for data science is nothing new, the JetBrains team having been doing great work there for some time, and the ecosystem just keeps getting better. You have all the building blocks you need to go from questions to answers in a very short time.

Kotlin Notebooks is a plugin you can add to the IDE you’re already familiar with, intelliJ, and can have you inspecting your dataset within minutes. The dataset does not have to be limited to niche hobbies. For example: has “the business” ever asked you a question where you knew you had the data but didn’t know how to turn it into actionable information? Like the rate of adoption of a new feature you launched?

### Getting Started

To get started just add the Kotlin Notebook plug-in from the plug-in marketplace in Intellij.

The next step is to connect your data source. A lot of examples you find online will have you import a csv and manipulate it in-memory as a dataframe. This is a fine approach for smaller datasets (up to 100,000 rows), but for larger datasets it can end up slow and impractical.

The Kotlin Dataframe library provides a number of convenient functions for accessing your datasource and manipulating the results as a dataframe. A dataframe is a tabular data structure, similar to a spreadsheet or an SQL table, consisting of rows and columns. It is a common and powerful abstraction used in data science.

The rest of this article is going to assume that you are following along with the [sample project](https://github.com/adele97/kotlin-notebooks-blog). However if you have a database you'd like to explore, you can of course connect it up and try to draw out your own insights!

The sample project looks at [particpation in the sport of powerlifting in 2023](https://www.kaggle.com/datasets/open-powerlifting/powerlifting-database?resource=download&select=openpowerlifting.csv).

Open up the sample project and take a look at `my-first-notebook.ipynb`

First, we import the Kotlin Dataframe and Kandy libraries.

```kotlin notebook
%use kandy
%use dataframe
```

[Kandy](https://kotlin.github.io/kandy/welcome.html) is an open-source plotting library for Kotlin based on the [lets-plot visalisation library](https://lets-plot.org/). Data is nice, but seeing is believing. Kandy helps us turn rows and columns full of numbners into actionable insights.


But before we can get plotting, we need data. We connect to the containerised Postgres database using a url, username and password. Then we can fetch the contents of the `powerlifting_data` table

```kotlin notebook
val dbConfig = DbConnectionConfig(URL, USER_NAME, PASSWORD)
val data = DataFrame.readSqlTable(dbConfig, "powerlifting_data")
```

Importing the entire table does defeat the purpose of creating a database to begin with, so I would not recommend making this your standard workflow (especially if you have more than 100,000 rows). But for today's exploratory purposes it's a fine place to start. And later I'll show you how to use SQL queries and dataframe together to work more efficently. 

### Understanding the data set

In order to do data science well, you need to understand your data set. Taking the time getting to know your data set helps you avoid making incorrect assumptions and drawing (costly) incorrect conclusions. I cannot stress enough the importance of this step! This is why I started my data science journey with powerlifing. It's a domain I have experienced first hand and it is much more than a data set to me. Let me show you what I mean. 

We can use the `describe()` function from Kotlin Dataframe on the fetched data.

```kotlin notebook
data.describe()
```

![](./describe.png)


The `describe()` function gives us some essential information about our powerlifitng dataset. The column names are listed on the left, in the name column alongside some summary statistics. Using the output we can say

- The data set has 179,227 rows.
- Of those, 104,960 are unique names (lifters).
- Name, sex, event, equipment, place, federation, date, country and meetname are mandatory fields
- All other fields are nullable (potentially empty)
- The most common event is SBD and with minimal equipment (aka Raw)

There are also some other curious things that may not be clear if you do not understand powerlifting
1. The minimum for the squat, bench and deadlift are all negative. This is how a missed lift is represented in the data set. A missed lift is one that was attempted during the competition but was not completed successfully, therefore it does not contribute to the final score, or total for the lifter. This convention makes the mean and median values from the `describe()` function largely useless
2. The entries squat4kg, bench4kg and deadlift4kg have a large number of null values. Fourth attempts are are, and do not count toward the TotalKg. They are used for recording single-lift records (eg: deadlift world record) and can only be taken under special circumstances. For most purposes, you can leave these values out of your analyses.


