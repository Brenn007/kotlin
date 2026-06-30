import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val localProps = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
val weatherBaseUrl: String = localProps.getProperty("weather.base.url")
    ?: System.getenv("WEATHER_BASE_URL")
    ?: "https://www.amonteiro.fr"
val weatherApiKey: String = localProps.getProperty("weather.api.key")
    ?: System.getenv("WEATHER_API_KEY")
    ?: ""

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    androidLibrary {
       namespace = "com.example.projet.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    // Generate BuildConfig with values from local.properties
    val generateBuildConfig by tasks.registering {
        notCompatibleWithConfigurationCache("reads local.properties at configuration time")
        val outputDir = layout.buildDirectory.dir("generated/buildconfig/commonMain/kotlin")
        inputs.property("weatherBaseUrl", weatherBaseUrl)
        inputs.property("weatherApiKey", weatherApiKey)
        outputs.dir(outputDir)
        doLast {
            val url = inputs.properties["weatherBaseUrl"] as String
            val key = inputs.properties["weatherApiKey"] as String
            val dir = outputDir.get().asFile
            dir.mkdirs()
            dir.resolve("BuildConfig.kt").writeText(
                "package com.example.projet\n\nobject BuildConfig {\n    const val WEATHER_BASE_URL = \"$url\"\n    const val WEATHER_API_KEY = \"$key\"\n}\n"
            )
        }
    }
    sourceSets.commonMain {
        kotlin.srcDir(generateBuildConfig.map { it.outputs.files })
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmTest.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}