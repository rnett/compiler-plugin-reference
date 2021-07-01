plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.rnett.compiler-plugin-importer")
}

dependencies {
    implementation("com.rnett.compiler-plugin-reference:import")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.20")

    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}

pluginImport {
    afterEvaluate {
        import(project(":runtime"), "test.generation") {
            rename("test", "test2")
        }
    }
}

