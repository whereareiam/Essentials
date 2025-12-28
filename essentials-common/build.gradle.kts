dependencies {
    "compileOnly"(project(":essentials-api"))
}

tasks.test {
    useJUnitPlatform()
}