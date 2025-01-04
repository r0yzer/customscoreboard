package de.royzer.customscoreboard.settings

import de.royzer.customscoreboard.configFile
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component


val json = Json {
    prettyPrint = true
}

object ScoreboardSettings {
    var showScoreboard = true
    var hideNumbers = false
    var backgroundOpacity = 0.3F
    var titleBackgroundOpacity = 0.4F
    var hideTitle = false
    var hiddenLines = mutableListOf<Int>()


    fun createGui(parent: Screen): Screen {
        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Scoreboard settings"))
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("General settings"))
                .tooltip(Component.literal("General Scoreboard settings"))
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Show scoreboard"))
//                        .tooltip(Component.literal("Select if the scoreboard should be displayed"))
                        .binding(
                            true,
                            { this.showScoreboard }
                        ) { newValue: Boolean ->
                            this.showScoreboard = newValue
                        }
                        .controller(BooleanControllerBuilder::create)
                        .build()
                )
                .option(
                    Option.createBuilder<Boolean>()
                        .name(Component.literal("Hide numbers"))
//                        .tooltip(Component.literal("Select if you want to hide the scores (numbers at the right)"))
                        .binding(
                            false,
                            { this.hideNumbers }
                        ) { newValue: Boolean ->
                            this.hideNumbers = newValue
                        }
                        .controller(BooleanControllerBuilder::create)
                        .build()
                )
                .build()
            )
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("Color settings"))
                .tooltip(Component.literal("Settings for the scoreboard colors/opacities"))
                .option(
                    Option.createBuilder<Float>()
                        .name(Component.literal("Title Background Opacity"))
//                        .tooltip(Component.literal("The opacity of the scoreboard title background"))
                        .binding(
                            0.4F,
                            { titleBackgroundOpacity },
                            { newValue: Float ->
                                titleBackgroundOpacity = newValue
                            }
                        )
                        .customController {
                            FloatSliderController(it, 0F, 1F, 0.1F)
                        }
                        .build()
                )
                .option(
                    Option.createBuilder<Float>()
                        .name(Component.literal("Background Opacity"))
//                        .tooltip(Component.literal("The opacity of the scoreboard background"))
                        .binding(
                            0.3F,
                            { backgroundOpacity },
                            { newValue: Float ->
                                backgroundOpacity = newValue
                            }
                        )
                        .customController {
                            FloatSliderController(it, 0F, 1F, 0.1F)
                        }
                        .build()
                )
                .build()
            )
            .category(
                ConfigCategory.createBuilder()
                    .name(Component.literal("Line settings"))
                    .tooltip(Component.literal("Select the lines you want to hide (lowest line = line 1)"))
                    .options(
                        lineOptions()
                    )
                    .build()
            )
            .save(ScoreboardSettings::save)
            .build()
            .generateScreen(parent)
    }

    private fun lineOptions(): List<Option<Boolean>> {
        val l = mutableListOf<Option<Boolean>>()
        l.add(
            Option.createBuilder<Boolean>()
//                .tooltip(Component.literal("Select if the scoreboard title should be hidden"))
                .name(Component.literal("Hide scoreboard title"))
                .binding(
                    false,
                    { hideTitle },
                    { newValue: Boolean ->
                        hideTitle = newValue
                    }
                )
                .controller(TickBoxControllerBuilder::create)
                .build()
        )
        repeat(15) {
            val i = it + 1
            l.add(Option.createBuilder<Boolean>()
                .name(Component.literal("Hide line $i"))
//                .tooltip(Component.literal("Select if line $i should be hidden"))
                .binding(
                    false,
                    { hiddenLines.contains(i) },
                    { newValue: Boolean ->
                        if (newValue)
                            hiddenLines.add(i)
                        else
                            hiddenLines.remove(i)
                    }
                )
                .controller(TickBoxControllerBuilder::create)
                .build()
            )
        }
        return l
    }

    fun load() {
        val c = try {
            Json.decodeFromString<ScoreboardSettingsFile>(configFile.readText())
        } catch (e: Exception) {
            save()
            return
        }
        showScoreboard = c.showScoreboard
        hideNumbers = c.hideNumbers
        backgroundOpacity = c.backgroundOpacity
        titleBackgroundOpacity = c.titleBackgroundOpacity
        hideTitle = c.hideTitle
        hiddenLines = c.hiddenLines
    }

    private fun save() {
        configFile.writeText(
            json.encodeToString(
                ScoreboardSettingsFile(
                    showScoreboard, hideNumbers, backgroundOpacity, titleBackgroundOpacity, hideTitle, hiddenLines
                )
            )
        )
    }
}