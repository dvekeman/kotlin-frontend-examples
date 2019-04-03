import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    id("kotlin2js") version "1.3.21"
    // id("kotlin-dce-js") version "1.3.21" // Dead code elimination. Advised but turned off for this example
    id("org.jetbrains.kotlin.frontend") version "0.0.45"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            outputFile = "${project.buildDir.path}/js/ktjs_example04.js"
            moduleKind = "umd"
            metaInfo = true
            sourceMap = true
            main = "call"
        }
    }
}

kotlinFrontend {
    downloadNodeJsVersion = "latest"

    npm {
        dependency("style-loader")
    }

    bundle<WebPackExtension>("webpack") {
        this as WebPackExtension
        bundleName = "ktjs_example04"
        mode = "development"
    }
}

