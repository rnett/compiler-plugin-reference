plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.dokka")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.20")
}

kotlin {
    sourceSets.all {
        languageSettings {
            useExperimentalAnnotation("kotlin.RequiresOptIn")
            useExperimentalAnnotation("com.rnett.plugin.InternalPluginReferenceAPI")
        }
    }
}