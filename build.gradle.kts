plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
}
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.realm.gradle.plugin)
    }
}