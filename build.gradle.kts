plugins {
    java
}

group = "me.codyq"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Minestom:Minestom:4e6c92e2")
    implementation("com.github.Bloepiloepi:MinestomPvP:5a55c39187")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
