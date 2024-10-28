plugins {
    `tectonic-common`
    alias(libs.plugins.freefair.aggregate.javadoc)
    alias(libs.plugins.axion.release)
}

version = scmVersion.version

dependencies {
    rootProject.subprojects.forEach { subproject ->
        subproject.plugins.withId("java") {
            javadoc(subproject)
        }
    }
}
