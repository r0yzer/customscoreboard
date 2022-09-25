package de.royzer.customscoreboard.settings

@kotlinx.serialization.Serializable
data class ScoreboardSettingsFile(
    var showScoreboard: Boolean,
    var hideNumbers: Boolean,
    var backgroundOpacity: Float,
    var titleBackgroundOpacity: Float,
    var hiddenLines: MutableList<Int>,
)