plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    kotlin("kapt")
    id("com.github.gmazzo.buildconfig")
    id("com.gradle.plugin-publish")
    id("org.jetbrains.dokka")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.5.20")

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")

    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

buildConfig {
    val project = project(":export-compiler-plugin")
    packageName("com.rnett.plugin")
    buildConfigField("String", "PROJECT_GROUP_ID", "\"${project.group}\"")
    buildConfigField("String", "PROJECT_ARTIFACT_ID", "\"${project.name}\"")
    buildConfigField("String", "PROJECT_VERSION", "\"${project.version}\"")
}

gradlePlugin {
    plugins {
        create("compilerPluginExporterPlugin") {
            id = "com.rnett.compiler-plugin-referenceer"
            displayName = "Compiler Plugin Export Plugin"
            description = "Compiler Plugin Export Plugin"
            implementationClass = "com.rnett.plugin.PluginExportGradlePlugin"
        }
    }
}