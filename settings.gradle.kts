rootProject.name = "tectonic"

include("common")


include("lang:yaml")
include("lang:toml")
include("lang:json")
include("lang:hocon")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")