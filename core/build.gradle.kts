plugins {
    alias(libs.plugins.kotlin)
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    annotationProcessor(libs.auto.value)
    compileOnly(libs.aapt2.proto)
    compileOnly(libs.androidx.annotation)
    compileOnly(libs.auto.value.annotations)
    compileOnly(libs.guava)
    implementation(libs.bundletool)
    implementation(libs.commons.codec)
    implementation(libs.protobuf.java)

    testCompileOnly(libs.guava)
    testImplementation(libs.junit)
}

publish {
    githubRepo = "color597/Shiroko"
    publications {
        register<MavenPublication>(rootProject.name) {
            artifactId = project.name
            group = group
            version = version
            from(components.getByName("java"))
            pom {
                name.set(project.name)
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
    }
}
