import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("fabric-loom") version "1.0-SNAPSHOT"
    //id("org.quiltmc.quilt-mappings-on-loom") version "4.2.1"
    id("io.github.juuxel.loom-quiltflower") version "1.7.3"
    id("com.modrinth.minotaur") version "2.4.3"
}

val minecraftVersion = "1.19.2"

group = "de.royzer"
version = "1.0"

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com/releases")
}
dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-1.19.2:2022.09.18@zip")
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.9")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.61.0+1.19.2")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.3+kotlin.1.7.10")

    include(modImplementation("dev.isxander:yet-another-config-lib:1.4.2")!!)
    modApi("com.terraformersmc:modmenu:4.0.5")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

modrinth {
    token.set(findProperty("modrinth.token").toString())
    projectId.set("HLW5dnBW")
    versionNumber.set(rootProject.version.toString())
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.set(listOf(minecraftVersion))
    loaders.add("fabric")

    dependencies.set(
        listOf(
            com.modrinth.minotaur.dependencies.ModDependency("P7dR8mSH", "required"),
            com.modrinth.minotaur.dependencies.ModDependency("Ha28R6CL", "required"),
            com.modrinth.minotaur.dependencies.ModDependency("mOgUt4GM", "optional")
        )
    )
}