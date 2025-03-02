plugins {
    `tectonic-common`
    `tectonic-compile`
    `tectonic-testing`
    `tectonic-publishing`
}

nyx.info {
    name = "Tectonic JSON"
}

dependencies {
    api(projects.common)

    api(libs.gson)
}

