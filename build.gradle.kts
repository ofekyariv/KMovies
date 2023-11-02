buildscript {
    val kotlinVersion = "1.9.10"
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.google.com/")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}
plugins {
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}