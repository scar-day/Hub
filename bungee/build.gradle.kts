plugins {
    id("net.minecrell.plugin-yml.bungee") version("0.6.0")
}

dependencies {
    implementation(project(":common"))

    implementation("eu.okaeri:okaeri-platform-bungee:0.4.39")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.4")

    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
}

bungee {
    main = "dev.scarday.Main"
    name = "Hub"
    version = rootProject.version.toString()
    author = "ScarDay"
}