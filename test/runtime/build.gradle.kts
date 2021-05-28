plugins {
    kotlin("multiplatform")
    id("com.rnett.compiler-plugin-referenceer")
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
    }
    sourceSets {
        val commonMain by getting {

        }
        val jvmMain by getting {

        }
    }
}

//dependencies {
//    implementation("com.rnett.compiler-plugin-reference:export")
//}

