import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("java")
    alias(libs.plugins.intelliJPlatform)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.changelog)
}

group = "io.github.vudsen.spelth"

val sha = providers.environmentVariable("HEAD_SHA").orNull
if (sha == null || sha.isEmpty()) {
    version = providers.gradleProperty("pluginVersion").get()
} else {
    version = providers.gradleProperty("pluginVersion").get() + "-$sha"
}


repositories {
    maven{ url=uri("https://maven.aliyun.com/repository/public") }
    maven{ url=uri("https://maven.aliyun.com/repository/google") }
    intellijPlatform {
        defaultRepositories()
    }
}
dependencies {
    intellijPlatform {
        intellijIdeaUltimate(providers.gradleProperty("platformVersion"))
        bundledPlugins("com.intellij.java", "com.intellij.spring")
    }
}
// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "241"
            untilBuild = "251.*"
        }

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("./README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }
    }


    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            val productReleases = ProductReleasesValueSource().get()
            val reducedProductReleases =
                if (productReleases.size > 2) listOf(productReleases.first(), productReleases.last())
                else productReleases
            ides(reducedProductReleases)
        }
    }

}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }

}