plugins {
    kotlin("jvm") version "1.5.10" apply false
    kotlin("kapt") version "1.5.10" apply false
    id("com.gradle.plugin-publish") version "0.15.0" apply false
    id("com.github.gmazzo.buildconfig") version "2.0.2" apply false
    id("com.rnett.compiler-plugin-referenceer") apply false
    id("com.rnett.compiler-plugin-importer") apply false
}

allprojects {

    group = "test"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

