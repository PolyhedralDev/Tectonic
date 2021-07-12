package com.dfsek.tectonic

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
