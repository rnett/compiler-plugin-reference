plugins {
    kotlin("jvm") version "1.5.10" apply false
    kotlin("multiplatform") version "1.5.10" apply false
    kotlin("kapt") version "1.5.10" apply false
    id("com.gradle.plugin-publish") version "0.15.0" apply false
    id("com.github.gmazzo.buildconfig") version "2.0.2" apply false
//    id("com.vanniktech.maven.publish") version "0.14.0" apply false
    id("org.jetbrains.dokka") version "1.4.30" apply false
    kotlin("plugin.serialization") version "1.5.10" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    signing
}

allprojects {
    group = "com.rnett.compiler-plugin-reference"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

//TODO set proper attributes
subprojects {
    afterEvaluate {
        extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension>()?.target {
            compilations.configureEach {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
                compileJavaTaskProvider.get().apply {
                    targetCompatibility = "1.8"
                    sourceCompatibility = "1.8"
                }
            }
        }
    }
}