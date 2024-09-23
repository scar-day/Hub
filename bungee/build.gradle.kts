dependencies {
    implementation("eu.okaeri:okaeri-platform-bungee:0.4.39")

    compileOnly("io.github.waterfallmc:waterfall-api:1.20-R0.1-SNAPSHOT")
}

repositories {
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
    mavenCentral()
}