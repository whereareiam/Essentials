defaultTasks("shadowJar")

allprojects {
    version = (System.getenv("VERSION") ?: "dev")

    apply(plugin = "java")

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    repositories {
        mavenCentral()

        maven("https://jitpack.io")
    }

    dependencies {
        // lombok
        "compileOnly"(rootProject.libs.lombok)
        "annotationProcessor"(rootProject.libs.lombok)

        // general
        "compileOnly"(rootProject.libs.guice)
        "compileOnly"(rootProject.libs.adventure)
        "compileOnly"(rootProject.libs.socialismus)

        // testing
        "testImplementation"(rootProject.libs.bundles.testing)
        "testImplementation"(rootProject.libs.guice)
        "testImplementation"(rootProject.libs.adventure)
        "testImplementation"(rootProject.libs.socialismus)
    }
}