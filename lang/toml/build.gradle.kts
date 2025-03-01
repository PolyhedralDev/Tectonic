plugins {
    `tectonic-common`
    `tectonic-compile`
    `tectonic-testing`
    `tectonic-publishing`
}

nyx.info {
    name = "Tectonic TOML"
}

dependencies {
    api(projects.common)

    api(libs.tomlj)
}


