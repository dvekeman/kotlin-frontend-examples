# Kotlin JS Examples

**Gradle Groovy vs Gradle Kotlin DSL**

**The master branch uses Kotlin DSL for gradle. The Groovy equivalent can be found on the `gradle_groovy` branch 
(but apart from the initial commit it will not be further updated)**

This repository shows how to interop between JavaScript and KotlinJS using different module systems.
The examples can be opened in a browser and don't require any server runtime (like NodeJs).

The main goal is to show interop between plain JS and JS transpiled from Kotlin.

The first three examples are the most basic ones and have the same structure. 
They only differ in the `moduleKind` setting (plain, amd, umd) and of 
course in the way they invoke the kotlin-generated JavaScript.

- 01-kt-plain-js
- 02-kt-amd-js
- 03-kt-umd-js

The rest of the examples use the `kotlin-frontend-plugin` on top of the `kotlinjs` plugin.

- 04-kt-frontend: Hello world for the `kotlin-frontend-plugin`
- 05-kt-frontend-vaadin: Example using [Vaadin Components], e.g. [Vaadin Grid Component]

## General Notes

All examples use the `kotlin2js` gradle plugin

```
plugins {
    id("kotlin2js") version "1.3.21"
}
```

The configuration is pretty straighforward

```
tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            outputFile = "${project.buildDir.path}/js/<name>.js"
            moduleKind = "< plain | amd | umd >"
            metaInfo = true
            sourceMap = true
            main = "call"
        }
    }
}
```

**Important remarks for the Kotlin Gradle DSL**

The plugin resolution is defined in the **settings.gradle.kts**:

```
pluginManagement {

    repositories {
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
        }

    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin2js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }

            if (requested.id.id == "org.jetbrains.kotlin.frontend") {
                useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:${requested.version}")
            }
        }
    }
}
```

The `kotlin2js` part is required for all examples. 
The `org.jetbrains.kotlin.frontend` case is only needed for example 4.

To run the examples:

```
./gradlew <example>:build
```

Then just **open the html file** from the example directory (plain.html, amd.html, umd.html or frontend.html) in a browser.

## Example 1: Kotlin JS using `plain` module

See [01-kt-plain-js/README.md](01-kt-plain-js/README.md)

## Example 2: Kotlin JS using `amd` module

See [02-kt-amd-js/README.md](02-kt-amd-js/README.md)

## Example 3: Kotlin JS using `umd` module

See [03-kt-umd-js/README.md](03-kt-umd-js/README.md)

## Example 4: Kotlin JS using the kotlin-frontend-plugin

See [04-kt-frontend/README.md](04-kt-frontend/README.md)

## Example 5: Using Vaadin Components

See [05-kt-frontend-vaadin/README.md](05-kt-frontend-vaadin/README.md)

# References

- [Kotlin JavaScript](https://kotlinlang.org/docs/reference/js-overview.html)
- [Kotlin Gradle Compile Options](https://github.com/JetBrains/kotlin/blob/master/ant/src/org/jetbrains/kotlin/ant/KotlinCompilerBaseTask.kt)
- [Kotlin Frontend plugin](https://github.com/Kotlin/kotlin-frontend-plugin)
- [RequireJS](https://requirejs.org/)
- [Webpack library and libraryTarget](https://webpack.js.org/guides/author-libraries/)
