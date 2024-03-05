import java.net.URI

val ktorVersion: String by project
val logbackClassicVersion: String by project
val exposedUtilsVersion: String by project
val postgresqlVersion: String by project
val hikariVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.21"
}

group = "com.github"
version = "1.0"

application {
    mainClass.set("com.github.rinha.ApplicationKt")
}

repositories {
    maven { url = URI("https://jitpack.io") }
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackClassicVersion")
    implementation("com.github.MarioFronza:exposed-utils:$exposedUtilsVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}