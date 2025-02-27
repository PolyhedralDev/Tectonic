import ca.solostudios.nyx.util.codeMC

plugins {
    `java-library`

    id("ca.solo-studios.nyx")
}

nyx {
    compile {
        jvmTarget = 21

        javadocJar = true
        sourcesJar = true

        allWarnings = true
        distributeLicense = true
        buildDependsOnJar = true
        reproducibleBuilds = true

        java {
            allJavadocWarnings = true
            noMissingJavadocWarnings = true
            javadocWarningsAsErrors = true
        }
    }
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