import ca.solostudios.nyx.util.codeMC
import gradle.kotlin.dsl.accessors._e054d9723d982fdb55b1e388b8ab0cbf.implementation

plugins {
    `java-library`

    id("ca.solo-studios.nyx")
}

nyx {
    compile {
        jvmTarget = 8

        javadocJar = true
        sourcesJar = true

        allWarnings = true
        distributeLicense = true
        buildDependsOnJar = true
        reproducibleBuilds = true
    }

    info {
        group = "com.dfsek.tectonic"
        version = rootProject.version.toString()
        description = """
            Tectonic is a powerful **read-only** Java configuration library for data-driven applications.
        """.trimIndent()

        organizationName = "Polyhedral Development"
        organizationUrl = "https://github.com/PolyhedralDev/"

        repository.fromGithub("PolyhedralDev", "Tectonic")

        developer {
            id = "dfsek"
            name = "dfsek"
            email = "dfsek@dfsek.com"
            url = "https://dfsek.com/"
        }
        developer {
            id = "duplexsys"
            name = "Zoë Gidiere"
            email = "duplexsys@protonmail.com"
            url = "https://solonovamax.gay"
        }
    }
}

repositories {
    mavenCentral()
    codeMC()
}

dependencies {
    implementation(libs.jetbrains.annotations)
}

tasks {
    withType<JavaCompile>().configureEach {
        options.isFork = true
        options.isIncremental = true
    }
}