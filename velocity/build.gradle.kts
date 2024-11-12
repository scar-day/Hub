dependencies {
    implementation(project(":common"))

    implementation("eu.okaeri:okaeri-platform-velocity:0.4.39")

    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
}