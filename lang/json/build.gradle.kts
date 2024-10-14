plugins {
    `tectonic-common`
    `tectonic-testing`
    `tectonic-publishing`
}

dependencies {
    api(projects.common)

    api(libs.gson)
}

