dependencyResolutionManagement {

    versionCatalogs {
        create("libs") {
            from(file("../gradle/libs.versions.toml"))
        }
    }
}