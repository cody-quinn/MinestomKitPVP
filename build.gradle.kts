plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "me.codyq"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Minestom:Minestom:3674fcc97d")
    implementation("com.github.Bloepiloepi:MinestomPvP:144a4ca1c2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        manifest {
            attributes (
                "Main-Class" to "me.codyq.minestomkitpvp.Main",
                "Multi-Release" to true
            )
        }

        archiveBaseName.set(project.name)
    }

    build {
        dependsOn(shadowJar)
    }
}
