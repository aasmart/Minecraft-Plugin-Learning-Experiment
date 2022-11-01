plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.5"
}

group = "PluginExperiment"
version = "1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("D:/Minecraft Server/plugins/PluginExperiment-${project.version}.jar"))
    }
}