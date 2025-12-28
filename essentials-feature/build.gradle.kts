subprojects {
    dependencies {
        "compileOnly"(project(":essentials-api"))
    }
}

dependencies {
    subprojects.forEach {
        "implementation"(it)
    }
}
