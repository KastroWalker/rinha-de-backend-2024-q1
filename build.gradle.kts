
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "kastro.dev"
version = "0.0.1"

application {
    mainClass.set("kastro.dev.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.postgresql:postgresql:42.7.1")
    implementation ("com.zaxxer:HikariCP:5.0.0")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("com.willowtreeapps.assertk:assertk:0.28.0")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "kastro.dev.ApplicationKt"
    }
}
