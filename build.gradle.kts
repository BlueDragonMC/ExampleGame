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
    implementation("com.github.BlueDragonMC.Server:common:e13cb0fda4")
    // BlueDragon's Minestom fork (make sure to use the same major Minestom version as BlueDragonMC/Server does)
    implementation("com.github.BlueDragonMC:Minestom:86b267195f")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    // BlueDragon's Server runs on Java 17
    kotlinOptions.jvmTarget = "17"
}
