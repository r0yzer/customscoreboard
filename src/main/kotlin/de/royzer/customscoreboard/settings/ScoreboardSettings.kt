package de.royzer.customscoreboard.settings

import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.isxander.yacl.gui.controllers.BooleanController
import dev.isxander.yacl.gui.controllers.string.StringController
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object ScoreboardSettings {
    var showScoreboard = true
    var hideNumbers = false

    var hiddenLines = mutableListOf<Int>()

    fun save() {}

    fun createGui(parent: Screen): Screen {
        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Fler"))
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("General settings"))
                .tooltip(Component.literal("General Scoreboard settings"))
                .option(
                    Option.createBuilder(Boolean::class.java)
                        .name(Component.literal("Show scoreboard"))
                        .binding(
                            true,
                            { this.showScoreboard }
                        ) { newValue: Boolean ->
                            this.showScoreboard = newValue
                        }
                        .controller(::BooleanController)
                        .build()
                )
                .option(
                    Option.createBuilder(Boolean::class.java)
                        .name(Component.literal("Hide numbers"))
                        .binding(
                            false,
                            { this.hideNumbers }
                        ) { newValue: Boolean ->
                            this.hideNumbers = newValue
                        }
                        .controller(::BooleanController)
                        .build()
                )
                .option(
                    Option.createBuilder(String::class.java)
                        .name(Component.literal("Hidden lines"))
                        .tooltip(Component.literal("Enter the lines you want to hide, seperated by a comma"))
                        .binding(
                            "",
                            { this.hiddenLines.joinToString() }
                        ) { newValue: String ->
                            try {
                                val splitStrings = newValue.removeSuffix(",").split(",")
                                val lines = splitStrings.map { it.trim() }.map { it.toInt() }.toMutableList()
                                this.hiddenLines = lines
                            } catch (e: Exception) {
                                this.hiddenLines = mutableListOf()
                            }
                        }
                        .controller(::StringController)
                        .build()
                )
                .build()
            )
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("andere settings"))
                .build()
            )
            .save(ScoreboardSettings::save)
            .build()
            .generateScreen(parent)
    }
}