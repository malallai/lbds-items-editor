import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    java
    alias(libs.plugins.shadow)
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0"
}

group = "fr.lacaleche.ie"
version = "1.0-SNAPSHOT"
description = "Paper plugin example"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.inventory.framework.paper)
    implementation(libs.inventory.framework.anvil.input)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        relocate("me.devnatan.inventoryframework", "${project.group}.shaded.inventoryframework")
    }
    
    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("1.19.4")
    }
}

bukkitPluginYaml {
    main = "fr.lacaleche.ie.ItemsEditor"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    apiVersion = "1.19"
    commands {
        register("itemseditor") {
            description = "ItemsEditor command"
            usage = "/itemseditor [item]"
            aliases = listOf("ie")
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
