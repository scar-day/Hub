plugins {
    id("net.minecrell.plugin-yml.bungee") version("0.6.0")
}

dependencies {
    implementation(project(":common"))

    implementation("eu.okaeri:okaeri-platform-bungee:0.4.39")

    compileOnly("io.github.waterfallmc:waterfall-api:1.20-R0.1-SNAPSHOT")
}

bungee {
    main = "dev.scarday.Main"
    name = "Hub"
    version = "2.1-BETA"
    author = "ScarDay"
}