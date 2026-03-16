package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onServerStart(CallbackInfo ci) {
        GameRuleData.load((MinecraftServer) (Object) this);
    }
}
