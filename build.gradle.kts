plugins {
    java
    `maven-publish`

    alias(libs.plugins.axion.release)
}

version = scmVersion.version