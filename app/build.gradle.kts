import groovy.json.JsonSlurper
import java.net.HttpURLConnection
import java.net.URL

fun updateVersion(major: Int, minor: Int) : String {
    val organization = "Narrow-Code"
    val repository = "WIG-Android"
    val apiUrl = "https://api.github.com/repos/$organization/$repository/releases/latest"
    println(apiUrl)

    val connection = URL(apiUrl).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val responseCode = connection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonSlurper = JsonSlurper()
        val jsonResponse = jsonSlurper.parseText(response) as Map<*, *>
        val latestTag = jsonResponse["tag_name"]
        val tagString = latestTag as? String ?: ""
        val pattern = "(\\d+)\\.(\\d+)\\.(\\d+)".toRegex() // Regex pattern for major.minor.patch
        val matchResult = pattern.find(tagString)
        val majorRelease = matchResult?.groupValues?.getOrNull(1)?.toInt() ?: 0 // Extract major version
        val minorRelease = matchResult?.groupValues?.getOrNull(2)?.toInt() ?: 0 // Extract minor version
        val patchRelease = matchResult?.groupValues?.getOrNull(3)?.toInt() ?: 0 // Extract patch version
        val patch = patchRelease + 1
        if (major > majorRelease) {
            return "$major.0.0"
        } else if (minor > minorRelease) {
            return "$major.$minor.0"
        }
        return "$major.$minor.$patch"
    } else {
        println("Failed to retrieve latest release tag. Response code: $responseCode")
        return "Patch version fail"
    }
}


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "wig"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    packaging {
        resources.excludes.addAll(listOf(
            "META-INF/LICENSE-notice.md",
            "META-INF/LICENSE.md"
        ))
    }

    defaultConfig {
        applicationId = "wig.wig.android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = updateVersion(0, 0)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = (true)
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    tasks.register("printVersionName") {
        doLast {
            println("${defaultConfig.versionName}")
        }
    }
}

dependencies {
    implementation("androidx.compose.runtime:runtime:1.6.0-beta01")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.yuriy-budiyev:code-scanner:2.3.2")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation ("androidx.test:core-ktx:1.5.0")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    implementation ("com.github.supersu-man:GitHubAPKUpdater-Library:v2.0.1")

    val ktorVersion = "1.6.3"
    implementation ("io.ktor:ktor-client-core:$ktorVersion")
    implementation ("io.ktor:ktor-client-android:$ktorVersion")
    implementation ("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation ("io.ktor:ktor-client-logging:$ktorVersion")
    implementation ("ch.qos.logback:logback-classic:1.2.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
}