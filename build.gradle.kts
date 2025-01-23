import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.1" apply false
    id("net.kyori.indra.git") version "3.1.3"

    `java-library`
    java
}

group = "dev.scarday"
version = "2.3"

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "net.kyori.indra.git")

    repositories {
        mavenCentral()

        maven { url = uri("https://repo.velocitypowered.com/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:24.1.0")

        implementation("eu.okaeri:okaeri-configs-core:5.0.5")

        implementation("net.kyori:adventure-text-minimessage:4.17.0")

        compileOnly("org.projectlombok:lombok:1.18.34")
        annotationProcessor("org.projectlombok:lombok:1.18.34")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<ShadowJar> {
        relocate("net.kyori", "dev.scarday.libs")
        relocate("eu.okaeri", "dev.scarday.libs")

        archiveFileName = "Hub-${rootProject.version}.jar"
    }

    sourceSets.main {
        java.srcDir(tasks.register<Copy>("generateTemplates") {
            inputs.property("version", rootProject.version)

            from("src/main/templates")
            into(layout.buildDirectory.dir("generated/sources/templates"))
            expand(mapOf("version" to rootProject.version))
        }.map {
            it.outputs.files
        })
    } //taken from LimboAuth Social Addon
}