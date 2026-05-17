package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleConfig;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

@Mixin(WorldCreationUiState.class)
public abstract class WorldCreationUiStateMixin {
    @Shadow
    private Difficulty difficulty;

    @Shadow
    public abstract void onChanged();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void applyLockedDefaultDifficulty(Path savesFolder, WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset, OptionalLong seed, CallbackInfo ci) {
        GameRuleConfig.getLockedDefaultDifficulty().ifPresent(difficulty -> this.difficulty = difficulty);
    }

    @Inject(method = "setDifficulty", at = @At("HEAD"), cancellable = true)
    private void preventLockedDefaultDifficultyChange(Difficulty difficulty, CallbackInfo ci) {
        GameRuleConfig.getLockedDefaultDifficulty().ifPresent(lockedDifficulty -> {
            this.difficulty = lockedDifficulty;
            this.onChanged();
            ci.cancel();
        });
    }

    @Inject(method = "getDifficulty", at = @At("HEAD"), cancellable = true)
    private void useLockedDefaultDifficulty(CallbackInfoReturnable<Difficulty> cir) {
        GameRuleConfig.getLockedDefaultDifficulty().ifPresent(cir::setReturnValue);
    }
}
