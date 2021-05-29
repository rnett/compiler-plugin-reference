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


    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    when {
        hostOs == "Mac OS X" -> {
            macosX64()
        }
        hostOs == "Linux" -> {
            linuxX64()
        }
        isMingwX64 -> {
            mingwX64()
            mingwX86()
        }
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    sourceSets {
    }
}