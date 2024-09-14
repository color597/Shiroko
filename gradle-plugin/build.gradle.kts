import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
    `maven-publish`
    signing
}

dependencies {
    // TODO: Replace with `libs.agp`
    compileOnly(libs.agp.impl)
    implementation(projects.core)
}

val generatedDir = File(projectDir, "generated")
val generatedJavaSourcesDir = File(generatedDir, "main/java")

val genTask = tasks.register("generateBuildClass") {
    inputs.property("version", version)
    outputs.dir(generatedDir)
    doLast {
        val buildClassFile =
            File(generatedJavaSourcesDir, "com/color597/shiroko/plugin/Build.java")
        buildClassFile.parentFile.mkdirs()
        buildClassFile.writeText(
            """
            package com.color597.shiroko.plugin;
            /**
             * The type Build.
             */
            public class Build {
               /**
                * The constant VERSION.
                */
               public static final String VERSION = "$version";
            }""".trimIndent()
        )
    }
}

sourceSets {
    main {
        java {
            srcDir(generatedJavaSourcesDir)
        }
    }
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn(genTask)
}

tasks.withType(Jar::class.java) {
    dependsOn(genTask)
}

tasks.named("kotlinSourcesJar") {
    dependsOn(genTask)
}

idea {
    module {
        generatedSourceDirs.add(generatedJavaSourcesDir)
    }
}

publish {
    githubRepo = "color597/Shiroko"
    publishPlugin("$group", rootProject.name, "com.color597.shiroko.plugin.ShirokoPlugin") {
        name.set(rootProject.name)
        description.set("Resource obfuscator for Android applications")
        url.set("ssh://git@github.com/color597/Shiroko")
        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://github.com/color597/Shiroko/blob/master/LICENSE.txt")
            }
        }
        developers {
            developer {
                name.set("Col_or")
                url.set("https://github.com/color597")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/color597/Shiroko.git")
            url.set("https://github.com/color597/Shiroko")
        }
    }
}
