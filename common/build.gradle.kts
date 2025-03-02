plugins {
    `tectonic-common`
    `tectonic-compile`
    `tectonic-testing`
    `tectonic-publishing`
}

nyx.info {
    name = "Tectonic Common"
}

dependencies {
    implementation(libs.commons.io)
}
