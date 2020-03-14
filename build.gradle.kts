import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.70"

    id("com.github.johnrengelman.shadow") version "4.0.4"
}

group = "com.github.iamthen0ise"
version = "stable"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "$kotlin_version")
    implementation("io.ktor", "ktor-server-netty", "$ktor_version")
    implementation("ch.qos.logback", "logback-classic", "$logback_version")
    implementation("io.ktor", "ktor-server-core", "$ktor_version")
    implementation("io.ktor", "ktor-locations", "$ktor_version")
    implementation("io.ktor", "ktor-metrics", "$ktor_version")
    implementation("io.ktor", "ktor-server-sessions", "$ktor_version")
    implementation("io.ktor", "ktor-auth", "$ktor_version")
    implementation("io.ktor", "ktor-auth-jwt", "$ktor_version")
    implementation("io.ktor", "ktor-serialization", "$ktor_version")

    implementation("org.jetbrains.exposed", "exposed", "0.12.1")
    implementation("com.zaxxer", "HikariCP", "2.7.8")
    implementation("org.postgresql", "postgresql", "42.2.2")

    implementation("org.flywaydb", "flyway-core", "5.2.4")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}