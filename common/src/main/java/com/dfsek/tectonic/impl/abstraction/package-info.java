/**
 * This package contains the Tectonic Abstraction API.
 * <p>
 * The Tectonic Abstraction API can be used to create configurations that depend on one another, by inheriting values
 * from "parent" configs. It has support for multiple inheritance, specifying which values to allow abstraction on,
 * and can detect errors such as circular inheritance.
 * <br>
 * To get started with the Abstraction API, make sure your configs include the keys:
 * <ul>
 *     <li>{@code id}: Required in all abstractable configs, to specify IDs to load others from</li>
 *     <li>{@code abstract}: Optional boolean key that defaults to false. If a config sets it to true, it will not be
 *     loaded as a config, rather it will be used only to pull values from. Abstract configs also do not need to contain
 *     all values defined by a {@code ConfigTemplate}, since they are only used to pull values from.</li>
 *     <li>{@code extends}: This optional key specifies the ID of another config that is to be used to get values missing
 *     from the current.</li>
 * </ul>
 *
 * @see com.dfsek.tectonic.api.loader.AbstractConfigLoader
 */
package com.dfsek.tectonic.impl.abstraction;
