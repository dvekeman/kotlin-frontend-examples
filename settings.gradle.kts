rootProject.name = "kotlin-frontend-examples"

include(":01-kt-plain-js")
include(":02-kt-amd-js")
include(":03-kt-umd-js")
include(":04-kt-frontend")
include(":05-kt-frontend-vaadin")

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
