import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "com.fitwithai.shared"
        compileSdk = 36
        minSdk = 26

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    val iosTargets = listOf(iosArm64(), iosSimulatorArm64())
    iosTargets.forEach { target ->
        target.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Re-export the whole shared graph so the iOS framework sees one umbrella.
            api(projects.domain)
            api(projects.data)
            api(projects.presentation)
            api(projects.designsystem)
            api(projects.core.common)
            api(projects.core.database)
            api(projects.core.datastore)
            api(projects.core.network)
            api(projects.core.platform)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        iosMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}
