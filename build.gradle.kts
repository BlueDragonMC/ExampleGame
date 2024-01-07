import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "com.bluedragonmc.games"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    // The "common" module of the BlueDragonMC/Server repository
    implementation("com.github.BlueDragonMC.Server:common:b11b152492")
    // BlueDragon's Minestom fork (make sure to use the same major Minestom version as BlueDragonMC/Server does)
    implementation("com.github.BlueDragonMC:minestom-ce:04f0919c9b")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    // BlueDragon's Server runs on Java 17
    kotlinOptions.jvmTarget = "17"
}
