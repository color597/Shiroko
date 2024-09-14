enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            library("aapt2-proto", "com.android.tools.build:aapt2-proto:8.6.0-11315950")
            library("agp", "com.android.tools.build:gradle-api:8.6.0")
            library("agp-impl", "com.android.tools.build:gradle:8.6.0")
            library("androidx-annotation", "androidx.annotation:annotation:1.8.2")
            library("auto-value", "com.google.auto.value:auto-value:1.11.0")
            library("auto-value-annotations", "com.google.auto.value:auto-value-annotations:1.11.0")
            library("bundletool", "com.android.tools.build:bundletool:1.17.1")
            library("commons-codec", "commons-codec:commons-codec:1.17.1")
            library("guava", "com.google.guava:guava:33.3.0-jre")
            library("junit", "junit:junit:4.13.2")
            library("protobuf-java", "com.google.protobuf:protobuf-java:3.21.12")
            plugin("lsplugin-publish", "org.lsposed.lsplugin.publish").version("1.1")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").version("2.0.20")
        }
    }
}
rootProject.name = "shiroko"

include(":core", ":gradle-plugin")

//include ':core', ':plugin', ':app', ':df_module1', ':df_module2'
//
//settings.project(":app").projectDir = file("$rootDir/samples/app")
//settings.project(":df_module1").projectDir = file("$rootDir/samples/dynamic-features/df_module1")
//settings.project(":df_module2").projectDir = file("$rootDir/samples/dynamic-features/df_module2")
//
