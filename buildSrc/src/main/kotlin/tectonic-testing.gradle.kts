plugins {
    `java-library`
}

dependencies {
    testImplementation(libs.bundles.junit)
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()

        maxHeapSize = "2G"
        ignoreFailures = false
        failFast = true
        maxParallelForks = (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
    }
}