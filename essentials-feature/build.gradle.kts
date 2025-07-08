subprojects {
    dependencies {
        "compileOnly"(project(":essentials-common-api"))
        "compileOnly"(rootProject.libs.bundles.cloud)
    }
}

dependencies {
    subprojects.forEach {
        "implementation"(it)
    }
}
