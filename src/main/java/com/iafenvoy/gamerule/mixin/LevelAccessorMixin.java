package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelAccessor.class)
public interface LevelAccessorMixin {
    @Inject(method = "getDifficulty", at = @At("HEAD"), cancellable = true)
    private void modifyLevelDifficulty(CallbackInfoReturnable<Difficulty> cir) {
        if ((Object) this instanceof Level level)
            GameRuleData.get(level.dimension()).map(GameRuleData.LevelDataEntry::getDifficulty).ifPresent(cir::setReturnValue);
    }
}
