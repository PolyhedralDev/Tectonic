plugins {
    `tectonic-common`
    `tectonic-compile`
    `tectonic-testing`
    `tectonic-publishing`
}

nyx.info {
    name = "Tectonic HOCON"
}

dependencies {
    api(projects.common)

    api(libs.lightbend.config)
}
