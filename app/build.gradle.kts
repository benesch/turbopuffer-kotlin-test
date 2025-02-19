plugins {
    alias(libs.plugins.kotlin.jvm)

    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.turbopuffer:turbopuffer-java:0.1.0-beta.0")
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
}
