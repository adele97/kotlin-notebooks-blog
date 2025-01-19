plugins {
    id("org.jetbrains.kotlinx.dataframe") version "0.15.0"
    kotlin("jvm") version "2.0.21"
    application
}

group = "com.gettingstarted"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.jetbrains.kotlinx:dataframe:0.15.0")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("DataImporterKt")
}