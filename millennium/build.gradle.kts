import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    android {
        namespace = "org.spotube.millennium"
        compileSdk = 37
        minSdk = 23
        androidResources.enable = true
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    jvm { compilerOptions { jvmTarget = JvmTarget.JVM_17 } }
    js {
        browser {
            testTask { useKarma { useFirefox() } }
        }
        binaries.executable()
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            api(libs.compose.foundation)
            implementation(libs.compose.resources)
            api(libs.composeunstyled)
            api(libs.composeunstyled.theming)
            api(libs.composeunstyled.primitives)
            api(libs.composeunstyled.colored.indication)
        }
        commonTest.dependencies { implementation(kotlin("test")) }
    }
}

mavenPublishing {
    publishToMavenCentral()
    coordinates("org.spotube.millennium", "millennium", "1.0.0")
    pom {
        name = "Millennium"
        description = "Millennium design system for Android Jetpack Compose and Compose Multiplatform"
        url = "https://github.com/team-spotube/millennium"
        licenses {
            license {
                name = "AGPL-3.0-or-later"
                url = "https://opensource.org/licenses/AGPL-3.0"
            }
        }
        developers {
            developer {
                id = "team-spotube"
                name = "Team Spotube"
                email = "team.spotube@proton.me"
            }
        }
        scm { url = "https://github.com/team-spotube/millennium" }
    }
    if (project.hasProperty("signing.keyId")) signAllPublications()
}
