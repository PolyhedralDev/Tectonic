plugins {
    `tectonic-common`
    `tectonic-compile`
    `tectonic-testing`
    `tectonic-publishing`
}

dependencies {
    api(projects.common)

    api(libs.snakeyaml)
}
