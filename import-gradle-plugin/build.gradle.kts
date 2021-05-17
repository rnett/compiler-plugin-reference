plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    kotlin("kapt")
    id("com.gradle.plugin-publish")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.5.0")
    implementation(project(":import-generator"))

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")

    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
gradlePlugin {
    plugins {
        create("compilerPluginImporterPlugin") {
            id = "com.rnett.compiler-plugin-importer"
            displayName = "Compiler Plugin Importer Plugin"
            description = "Compiler Plugin Importer Plugin"
            implementationClass = "com.rnett.plugin.PluginImportGradlePlugin"
        }
    }
}