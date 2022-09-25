package de.royzer.customscoreboard.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.royzer.customscoreboard.settings.ScoreboardSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {

    @Shadow
    private int screenWidth;

    @Inject(
        method = "displayScoreboardSidebar",
        at = @At("HEAD"),
        cancellable = true
    )
    public void disableScoreboard(PoseStack poseStack, Objective objective, CallbackInfo ci) {
        if (!ScoreboardSettings.INSTANCE.getShowScoreboard()) {
            ci.cancel();
        }
    }

    @ModifyArg(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;draw(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/String;FFI)I"
        ),
        index = 1
    )
    public String removeNumbers(String text) {
        if (ScoreboardSettings.INSTANCE.getHideNumbers()) return "";
        else return text;
    }

    @ModifyArg(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;draw(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/String;FFI)I"
        ),
        index = 2
    )
    public float correctScoreboardPosition(float x) {
        if (ScoreboardSettings.INSTANCE.getHideNumbers()) return this.screenWidth - 3 + 2;
        else return x;
    }

    @Redirect(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;getBackgroundColor(F)I",
            ordinal = 0
        )
    )
    public int setBackgroundOpacity(Options instance, float opacity) {
        return Minecraft.getInstance().options.getBackgroundColor(ScoreboardSettings.INSTANCE.getBackgroundOpacity());
    }

    @Redirect(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;getBackgroundColor(F)I",
            ordinal = 1
        )
    )
    public int setTitleBackgroundOpacity(Options instance, float opacity) {
        return Minecraft.getInstance().options.getBackgroundColor(ScoreboardSettings.INSTANCE.getTitleBackgroundOpacity());
    }



}
