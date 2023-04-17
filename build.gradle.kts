plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}

repositories {
    mavenCentral()
    google()
}


group = "dev.bitspittle"
version = libs.versions.firebase.bindings.get()

kotlin {
    js(IR) {
        browser()
    }

    @Suppress("UNUSED_VARIABLE") // Suppress spurious warnings about sourceset variables not being used
    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("firebase", libs.versions.firebase.web.get()))
                implementation(libs.kotlinx.coroutines)
            }
        }
    }
}

publishing {
    repositories {
        maven {
            group = project.group
            version = project.version.toString()
        }
    }
}

