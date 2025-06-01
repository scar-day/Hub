plugins {
    id("java")

    id("com.gradleup.shadow") version "8.3.1"
}

group = "dev.scarday"
version = "1.0"

repositories {
    mavenCentral()

    maven("https://repo.velocitypowered.com/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    implementation("eu.okaeri:okaeri-configs-core:5.0.5")
    implementation("eu.okaeri:okaeri-platform-velocity:0.4.39")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.shadowJar {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("")

    relocate("eu.okaeri", "dev.scarday.libs")
}