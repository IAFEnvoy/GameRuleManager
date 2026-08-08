package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleConfig;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.class)
public class GameRulesMixin {
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void setDefaultRules(CallbackInfo ci) {
        GameRuleConfig.getDefault().apply((GameRules) (Object) this);
    }
}
