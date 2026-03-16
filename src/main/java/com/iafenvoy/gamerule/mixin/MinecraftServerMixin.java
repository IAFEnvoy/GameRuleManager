package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Unique
    private MinecraftServer gameRuleManager$self() {
        return (MinecraftServer) (Object) this;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onServerStart(CallbackInfo ci) {
        GameRuleData.load(this.gameRuleManager$self());
    }

    @Inject(method = "loadLevel", at = @At("RETURN"))
    private void afterWorldLoad(CallbackInfo ci) {
        GameRuleData.forceSetLockRules(this.gameRuleManager$self());
    }
}
