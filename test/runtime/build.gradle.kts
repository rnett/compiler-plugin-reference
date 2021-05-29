plugins {
    kotlin("multiplatform")
    id("com.rnett.compiler-plugin-referenceer")
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" -> macosX64("native")
//        hostOs == "Linux" -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }

    val native1 = mingwX64()
    val native2 = mingwX86()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.rnett.compiler-plugin-reference:export")
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain)
            sourceSets[native1.name + "Main"].dependsOn(this)
            sourceSets[native2.name + "Main"].dependsOn(this)
        }
        val jvmMain by getting {

        }
    }
}

