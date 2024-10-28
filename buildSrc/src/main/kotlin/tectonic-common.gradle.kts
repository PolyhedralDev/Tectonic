import ca.solostudios.nyx.util.codeMC

plugins {
    id("ca.solo-studios.nyx")
}

nyx {
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
}
