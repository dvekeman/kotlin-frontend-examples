import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    id("kotlin2js") version "1.3.21"
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
            outputFile = "${project.buildDir.path}/js/ktjs_example02.js"
            moduleKind = "amd"
            metaInfo = true
            sourceMap = true
            main = "call"
        }
    }
}
