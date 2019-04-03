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
            outputFile = "${project.buildDir.path}/js/ktjs_example03.js"
            moduleKind = "umd"
            metaInfo = true
            sourceMap = true
            main = "call"
        }
    }
}
