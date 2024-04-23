// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.7.0"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51")
    }
}



//buildscript {
//    dependencies {
//        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51")
//    }
//}