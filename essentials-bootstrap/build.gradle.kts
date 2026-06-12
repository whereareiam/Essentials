plugins {
    id("runtime")
}

dependencies {
    implementation(project(":essentials-api"))
    implementation(project(":essentials-command"))
    implementation(project(":essentials-common"))
    implementation(project(":essentials-feature"))
}
