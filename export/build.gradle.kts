plugins {
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

//TODO should be all targets
kotlin {
    jvm()
    js(IR) {
        nodejs()
        browser()
    }
    sourceSets {
    }
}