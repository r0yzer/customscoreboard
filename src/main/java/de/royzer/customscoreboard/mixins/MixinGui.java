package de.royzer.customscoreboard.mixins;

import de.royzer.customscoreboard.settings.ScoreboardSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui  {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
        method = "displayScoreboardSidebar",
        at = @At("HEAD"),
        cancellable = true
    )
    public void disableScoreboard(GuiGraphics guiGraphics, Objective objective, CallbackInfo ci) {
        if (!ScoreboardSettings.INSTANCE.getShowScoreboard()) {
            ci.cancel();
        }
    }

    @ModifyArg(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
            ordinal = 2
        ),
        index = 1
    )
    public Component removeNumbers(Component component) {
        if (ScoreboardSettings.INSTANCE.getHideNumbers()) return Component.literal("");
        else return component;
    }

    @ModifyArg(
            method = "displayScoreboardSidebar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 2
            ),
            index = 2
    )
    public int correctScoreboardPosition(int i) {
        if (ScoreboardSettings.INSTANCE.getHideNumbers()) return this.minecraft.getWindow().getScreenWidth() - 3 + 2;
        else return i;
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

    // hide title
    @Redirect(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
            ordinal = 0
        )
    )
    public int hideTitle(GuiGraphics instance, Font font, Component component, int i, int j, int k, boolean bl) {
        if (!ScoreboardSettings.INSTANCE.getHideTitle()) {
            instance.drawString(font, component, i, j, k);
        }
        return 0;
    }

    @Redirect(
        method = "displayScoreboardSidebar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V",
            ordinal = 0
        )
    )
    public void hideTitleBackground(GuiGraphics instance, int i, int j, int k, int l, int m) {
        if (!ScoreboardSettings.INSTANCE.getHideTitle()) {
            instance.fill(i, j, k, l, m);
        }
    }

}
