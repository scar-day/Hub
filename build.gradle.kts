import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.1" apply false
    id("net.kyori.indra.git") version "3.1.3"

    `java-library`
    java
}

group = "dev.scarday"
version = "2.2"

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
        implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")

        compileOnly("org.projectlombok:lombok:1.18.34")
        annotationProcessor("org.projectlombok:lombok:1.18.34")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<ShadowJar> {
        val commitId = indraGit.commit()?.abbreviate(7)?.name() ?: "UNKNOWN"

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        val buildName = "${rootProject.name}-${project.name}-($commitId).jar"

        archiveFileName.set(buildName)
        destinationDirectory.set(file("${rootProject.projectDir}/compile"))

        doFirst {
            System.setProperty("file.encoding", "UTF-8")
        }

        if (project.name == "common") {
            enabled = false
        }
    }

    tasks.named("clean") {
        doLast {
            delete(file("${rootProject.projectDir}/compile"))
        }
    }

    tasks.withType<ProcessResources> {
        include("**/*.yml")
        include("**/*.prop")
        include("**/*.zip")
        filter<org.apache.tools.ant.filters.ReplaceTokens>(
            "tokens" to mapOf(
                "ID" to rootProject.name.lowercase(),
                "NAME" to rootProject.name,
                "VERSION" to rootProject.version,
                "AUTHOR" to "ScarDay"
            )
        )
    }

    if (project.name == "velocity") {
        tasks.named<ProcessResources>("processResources") {
            from(sourceSets["main"].resources.srcDirs) {
                include("velocity-plugin.json")
            }
        }
    }
}
