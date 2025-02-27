# Tectonic

[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.solo-studios.ca%2Fjob%2FPolyhedralDev%2Fjob%2FTectonic%2Fjob%2Fmaster%2F&style=for-the-badge&link=https%3A%2F%2Fci.solo-studios.ca%2Fjob%2FPolyhedralDev%2Fjob%2FTectonic%2Fjob%2Fmaster%2F)](https://ci.solo-studios.ca/job/PolyhedralDev/job/Tectonic/job/master/)
[![GitHub Tag](https://img.shields.io/github/v/tag/PolyhedralDev/Tectonic?sort=semver&style=for-the-badge)](https://github.com/PolyhedralDev/Tectonic/tags)
[![Chat](https://img.shields.io/discord/715448651786485780?style=for-the-badge&color=7389D8)](https://terra.polydev.org/contact.html)

<big><b>A data-driven Java configuration library serving as the base for [Terra's](https://github.com/PolyhedralDev/Terra)
extensive config system.</b></big>

## About

Tectonic is a powerful **read-only** Java configuration library for data-driven applications. It supports many
configuration languages, and is simple to implement for new ones.

Tectonic has a powerful abstraction system, making it an extremely useful tool for data-driven applications where users
may be creating many very similar configurations.

Tectonic has an extensive type loading system, with automatic support for generic types.

# Supported config languages

* YAML - [SnakeYAML](https://github.com/asomov/snakeyaml)
* JSON - [Gson](https://github.com/google/gson)
* TOML - [tomlj](https://github.com/tomlj/tomlj)
* HOCON - [lightbend](https://github.com/lightbend/config)

## Licensing

Tectonic is licensed under the [MIT License](https://github.com/PolyhedralDev/Tectonic/blob/master/LICENSE).