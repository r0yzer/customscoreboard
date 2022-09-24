package de.royzer.customscoreboard.mixins;

import com.google.common.collect.Lists;
import de.royzer.customscoreboard.settings.ScoreboardSettings;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(Scoreboard.class)
public class MixinScoreboard {

    @Final
    @Shadow
    private Map<String, Map<Objective, Score>> playerScores;

    @Inject(
        method = "getPlayerScores(Lnet/minecraft/world/scores/Objective;)Ljava/util/Collection;",
        at = @At("HEAD"),
        cancellable = true
    )
    public void setCustomLines(Objective objective, CallbackInfoReturnable<Collection<Score>> cir) {
        if (!ScoreboardSettings.INSTANCE.getHiddenLines().isEmpty()) {
            // copied from the real function
            List<Score> list = Lists.newArrayList();
            int i = 0;
            for(Map<Objective, Score> map : this.playerScores.values()) {
                Score score = map.get(objective);
                if (score != null) {
                    if (!ScoreboardSettings.INSTANCE.getHiddenLines().contains(i+1)) {
                        list.add(score);
                    }
                }
                i++;
            }
            list.sort(Score.SCORE_COMPARATOR);
            cir.setReturnValue(list);
        }
    }

}
