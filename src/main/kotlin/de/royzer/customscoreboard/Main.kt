package de.royzer.customscoreboard

import de.royzer.customscoreboard.settings.ScoreboardSettings
import net.minecraft.client.Minecraft
import java.io.File

val configFile = File(Minecraft.getInstance().gameDirectory.path + "/config", "customscoreboard.json")

fun initClient() {
    if (!configFile.exists()) configFile.createNewFile()
    ScoreboardSettings.load()
}