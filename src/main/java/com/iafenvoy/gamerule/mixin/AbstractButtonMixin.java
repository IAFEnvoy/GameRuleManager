package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractButton.class)
public abstract class AbstractButtonMixin extends AbstractWidget {
    protected AbstractButtonMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void disableLockedDefaultDifficultyButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.gameRuleManager$isLockedDefaultDifficultyButton())
            this.active = false;
    }

    private boolean gameRuleManager$isLockedDefaultDifficultyButton() {
        if (GameRuleConfig.getLockedDefaultDifficulty().isEmpty()
                || !(Minecraft.getInstance().screen instanceof CreateWorldScreen)
                || !((Object) this instanceof CycleButton<?>))
            return false;
        Component name = ((CycleButtonAccessor) this).gameRuleManager$getName();
        return name.getContents() instanceof TranslatableContents contents && "options.difficulty".equals(contents.getKey());
    }
}
