import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("fabric-loom") version "1.2-SNAPSHOT"
    //id("org.quiltmc.quilt-mappings-on-loom") version "4.2.1"
    id("io.github.juuxel.loom-quiltflower") version "1.9.0"
    id("com.modrinth.minotaur") version "2.8.1"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

val minecraftVersion = "1.20.1"

group = "de.royzer"
version = "1.0.1"

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com/releases")
}
dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())


    modImplementation("net.fabricmc:fabric-loader:0.14.21")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.83.1+1.20.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.5+kotlin.1.8.22")

    include(modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:3.0.2+1.20")!!)
    modApi("com.terraformersmc:modmenu:7.0.1")
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
    loaders.addAll(listOf("fabric", "quilt"))

    dependencies.set(
        listOf(
            com.modrinth.minotaur.dependencies.ModDependency("P7dR8mSH", "required"),
            com.modrinth.minotaur.dependencies.ModDependency("Ha28R6CL", "required"),
            com.modrinth.minotaur.dependencies.ModDependency("mOgUt4GM", "required")
        )
    )
}

curseforge {
    apiKey = findProperty("curseforge.token") ?: ""
    project(closureOf<com.matthewprenger.cursegradle.CurseProject> {
        mainArtifact(tasks.getByName("remapJar").outputs.files.first())

        id = "681641"
        releaseType = "release"
        addGameVersion(minecraftVersion)

        relations(closureOf<com.matthewprenger.cursegradle.CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
            optionalDependency("modmenu")
        })
    })
    options(closureOf<com.matthewprenger.cursegradle.Options> {
        forgeGradleIntegration = false
    })
}