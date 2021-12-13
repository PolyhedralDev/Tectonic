plugins {
    `java-library`
    `maven-publish`
}


repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()

    maxHeapSize = "4G"
    ignoreFailures = false
    failFast = true
    maxParallelForks = 12
}

dependencies {
    compileOnly("org.jetbrains:annotations:20.1.0")
    testImplementation("org.jetbrains:annotations:20.1.0")
    api(project(":common"))

    implementation("org.yaml:snakeyaml:1.27")

    // JUnit.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}


