package de.royzer.customscoreboard.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.royzer.customscoreboard.settings.ScoreboardSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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

}
