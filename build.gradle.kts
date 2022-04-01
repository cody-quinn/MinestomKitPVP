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
    implementation("com.github.Minestom:Minestom:4e6c92e2")
    implementation("com.github.Bloepiloepi:MinestomPvP:5a55c39187")
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
