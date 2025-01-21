plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.codepoetics"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.codepoetics:mariko-core:1.0-SNAPSHOT")
    implementation("com.codepoetics:mariko-kotlin:1.0-SNAPSHOT")
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}