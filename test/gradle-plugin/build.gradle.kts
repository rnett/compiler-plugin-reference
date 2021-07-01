plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    kotlin("kapt")
    id("com.github.gmazzo.buildconfig")
    id("com.gradle.plugin-publish")
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
    val project = project(":compiler-plugin")
    packageName("test.plugin")
    buildConfigField("String", "PROJECT_GROUP_ID", "\"${project.group}\"")
    buildConfigField("String", "PROJECT_ARTIFACT_ID", "\"${project.name}\"")
    buildConfigField("String", "PROJECT_VERSION", "\"${project.version}\"")
}

gradlePlugin {
    plugins {
        create("compilerPluginExporterPlugin") {
            id = "test.gradle-plugin"
            displayName = "Compiler Plugin Export Plugin"
            description = "Compiler Plugin Export Plugin"
            implementationClass = "test.plugin.TestGradlePlugin"
        }
    }
}