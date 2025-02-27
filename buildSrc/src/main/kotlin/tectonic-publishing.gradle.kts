import ca.solostudios.nyx.util.reposiliteMaven

plugins {
    java
    `maven-publish`

    id("ca.solo-studios.nyx")
}

nyx {
    publishing {
        withSignedPublishing()

        repositories {
            maven {
                name = "Sonatype"

                val repositoryId: String? by project
                url = when {
                    repositoryId != null -> uri("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repositoryId/")
                    else -> uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }

                credentials(PasswordCredentials::class)
            }
            maven("https://repo.codemc.io/repository/maven-releases/") {
                name = "CodeMC"
                credentials(PasswordCredentials::class)
            }
            reposiliteMaven("https://maven.solo-studios.ca/releases/") {
                name = "SoloStudiosReleases"
                credentials(PasswordCredentials::class)
            }
            reposiliteMaven("https://maven.solo-studios.ca/snapshots/") {
                name = "SoloStudiosSnapshots"
                credentials(PasswordCredentials::class)
            }
        }
    }
}