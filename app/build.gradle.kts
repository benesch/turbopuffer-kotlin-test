plugins {
    alias(libs.plugins.kotlin.jvm)

    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.turbopuffer.api:turbopuffer-kotlin") {
        version {
            branch = "main"
        }
    }
    implementation("io.exoquery:pprint-kotlin:2.0.2")
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.AppKt"
}
