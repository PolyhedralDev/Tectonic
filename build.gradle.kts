import com.dfsek.tectonic.getGitHash

plugins {
    java
    `maven-publish`
}

val versionObj = Version("3", "0", "1", false)

allprojects {
    version = versionObj
    group = "com.dfsek.tectonic"

    tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
        options.isIncremental = true
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()

        maxHeapSize = "2G"
        ignoreFailures = false
        failFast = true
        maxParallelForks = (Runtime.getRuntime().availableProcessors() - 1).takeIf { it > 0 } ?: 1

        reports.html.required.set(false)
        reports.junitXml.required.set(false)
    }

    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:22.0.0")
        testCompileOnly("org.jetbrains:annotations:22.0.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    @Suppress("UNUSED_VARIABLE")
    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }

        repositories {
            val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"

            maven(mavenUrl) {
                val mavenUsername: String? by project
                val mavenPassword: String? by project
                if (mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}

/**
 * Version class that does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(val major: String, val minor: String, val revision: String, val preRelease: Boolean = false) {

    override fun toString(): String {
        return if (!preRelease)
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision+${getGitHash()}"
    }
}