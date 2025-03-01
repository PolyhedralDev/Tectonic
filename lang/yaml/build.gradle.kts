plugins {
    `tectonic-common`
    `tectonic-compile`
    `tectonic-testing`
    `tectonic-publishing`
}

nyx.info {
    name = "Tectonic YAML"
}

dependencies {
    api(projects.common)

    api(libs.snakeyaml)
}
