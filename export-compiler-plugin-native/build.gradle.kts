plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler:1.5.10")
    implementation(project(":core", configuration = "shadow"))


    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
}

kotlin {
    this.target {
        mavenPublication {
            project.shadow.component(this)
        }
    }
}

tasks.shadowJar.configure {
    archiveClassifier.set("")
    dependencies {
        include(dependency(":core"))
        exclude(dependency("org.jetbrains.annotations:.*"))
        exclude(dependency("org.intellij.lang.annotations:.*"))
    }
}

tasks.jar.configure {
    finalizedBy(tasks.shadowJar)
}

tasks.named("compileKotlin") { dependsOn("syncSource") }
tasks.register<Sync>("syncSource") {
    from(project(":export-compiler-plugin").sourceSets.main.get().allSource)
    into("src/main/kotlin")
    filter {
        // Replace shadowed imports from plugin module
        when (it) {
            "import org.jetbrains.kotlin.com.intellij.mock.MockProject" -> "import com.intellij.mock.MockProject"
            else -> it
        }
    }
}