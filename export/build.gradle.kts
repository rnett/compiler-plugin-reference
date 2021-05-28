plugins {
    kotlin("multiplatform")
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