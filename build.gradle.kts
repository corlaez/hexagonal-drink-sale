plugins {
    kotlin("jvm") version "2.4.10"
    application
    id("com.gradleup.shadow") version "9.6.1"
}

group = "com.corlaez.learn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jline:jline:4.3.1")// fancy console
    implementation("io.javalin:javalin:7.2.2")// http server
    implementation("com.fasterxml.uuid:java-uuid-generator:5.2.0")// generate ids
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.2")// javalin json
    implementation("tools.jackson.module:jackson-module-kotlin:3.0.0")// jackson kotlin
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")// javalin requires a slf4j impl to be present
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

tasks {
    shadowJar {
        // Keeps the output JAR name clean: build/libs/your-app-1.0-SNAPSHOT.jar
        archiveClassifier.set("")

        manifest {
            // Prevents ugly warnings due to jline library
            attributes["Enable-Native-Access"] = "ALL-UNNAMED"
        }
    }
}
