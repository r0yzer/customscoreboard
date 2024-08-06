package de.royzer.customscoreboard.mixins;

import com.google.common.collect.Lists;
import de.royzer.customscoreboard.settings.ScoreboardSettings;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerScores;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(Scoreboard.class)
public class MixinScoreboard {

    @Final
    @Shadow
    private Map<String, PlayerScores> playerScores;

    @Inject(
        method = "listPlayerScores(Lnet/minecraft/world/scores/Objective;)Ljava/util/Collection;",
        at = @At("HEAD"),
        cancellable = true
    )
    public void setCustomLines(Objective objective, CallbackInfoReturnable<Collection<PlayerScoreEntry>> cir) {
        if (!ScoreboardSettings.INSTANCE.getHiddenLines().isEmpty()) {
            // copied from the real function
            List<PlayerScoreEntry> list = new ArrayList<>();
            this.playerScores.forEach((string, playerScores) -> {
                Score score = playerScores.get(objective);
                if (score != null) {
                    list.add(new PlayerScoreEntry(string, score.value(), score.display(), score.numberFormat()));
                }

            });
            // filter lines to be hidden
            List<PlayerScoreEntry> list2 = new ArrayList<>();
            var i = 0;
            for (PlayerScoreEntry score : list) {
                i++;
                if (!ScoreboardSettings.INSTANCE.getHiddenLines().contains(i)) {
                    list2.add(score);
                }
            }

            cir.setReturnValue(list2);
        }
    }

}
