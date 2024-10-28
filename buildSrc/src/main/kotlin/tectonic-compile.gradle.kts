import ca.solostudios.nyx.util.codeMC

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