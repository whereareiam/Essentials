java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "Essentials"
            pom {
                name.set("Essentials")
                description.set("Public API for Essentials - Socialismus essentials module")
            }
        }
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:none", "-quiet")
        title = "Essentials API"
        windowTitle = "Essentials API"
    }
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            val realm = (System.getenv("PUBLISH_REALM")
                ?: if ((System.getenv("VERSION") ?: "dev").contains("dev", true)) "development" else "release")
                .lowercase()
            url = uri("https://maven.whereareiam.me/$realm")
            credentials {
                username = System.getenv("PUBLISH_USER") ?: ""
                password = System.getenv("PUBLISH_TOKEN") ?: ""
            }
        }
    }
}