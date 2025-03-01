plugins {
    id("ca.solo-studios.nyx")
}

nyx {
    info {
        module = "tectonic${project.path.replace(':', '-').trimEnd('-')}"
        group = "com.dfsek"
        version = rootProject.version.toString()
        description = """
            Tectonic is a powerful **read-only** Java configuration library for data-driven applications.
        """.trimIndent()

        organizationName = "Polyhedral Development"
        organizationUrl = "https://github.com/PolyhedralDev/"

        repository.fromGithub("PolyhedralDev", "Tectonic")

        license.useMIT()

        developer {
            id = "dfsek"
            name = "dfsek"
            email = "dfsek@dfsek.com"
            url = "https://dfsek.com/"
        }
        developer {
            id = "duplexsystem"
            name = "Zoë Gidiere"
            email = "duplexsys@protonmail.com"
            url = "https://duplexsystem.org/"
        }
    }
}

repositories {
    mavenCentral()
}
