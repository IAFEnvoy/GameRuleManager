package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
//? >=1.21.2 {
import net.minecraft.server.level.ServerLevel;
//?}
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(/*? >=1.21.2 {*/ServerLevel/*?} else {*//*Level*//*?}*/.class)
public abstract class ServerLevelMixin {
    @Inject(method = "getGameRules", at = @At("HEAD"), cancellable = true)
    private void modifyLevelGameRules(CallbackInfoReturnable<GameRules> cir) {
        GameRuleData.get(((Level) (Object) this).dimension()).map(GameRuleData.LevelDataEntry::getGameRules).ifPresent(cir::setReturnValue);
    }
}
