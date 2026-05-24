import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    java
}

group = "io.github.kcajpanda"
version = "0.1.4"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withJavadocJar()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.63-stable")
    compileOnly("de.oliver:FancyNpcs:2.10.0")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    withType<Javadoc>().configureEach {
        options.encoding = "UTF-8"
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }
}
