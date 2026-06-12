plugins {
    id("shared")
}

dependencies {
    compileOnly(rootProject.libs.commandant)
    compileOnly(rootProject.libs.cloud.annotations)
    testImplementation(rootProject.libs.socialismus.module.api)
}
