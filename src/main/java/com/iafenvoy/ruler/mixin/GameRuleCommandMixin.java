package com.iafenvoy.ruler.mixin;

import com.google.gson.JsonPrimitive;
import com.iafenvoy.ruler.GameRuleConfig;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GameRuleCommand;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRuleCommand.class)
public class GameRuleCommandMixin {
    @Inject(method = "setRule", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules$Value;setFromArgument(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)V"), cancellable = true)
    private static <T extends GameRules.Value<T>> void onSetGameRule(CommandContext<CommandSourceStack> context, GameRules.Key<T> key, CallbackInfoReturnable<Integer> cir) {
        JsonPrimitive primitive = GameRuleConfig.INSTANCE.getData(key.getId());
        if (primitive != null) {
            context.getSource().sendSuccess(() -> Component.literal("This game rule has been locked by The Ruler, unlock it by removing corresponding key in config."), false);
            cir.setReturnValue(0);
        }
    }
}
