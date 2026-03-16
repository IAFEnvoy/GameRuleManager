package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
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
        CommandSourceStack source = context.getSource();
        if (GameRuleData.isLocked(source.getLevel().dimension(), key.getId())) {
            source.sendSuccess(() -> Component.literal("This gamerule has been locked by GameRule Manager, unlock it by changing lock key in value to false."), false);
            cir.setReturnValue(0);
        }
    }

    @ModifyExpressionValue(method = "queryRule", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getGameRules()Lnet/minecraft/world/level/GameRules;"))
    private static GameRules changeQueryGameRules(GameRules original, @Local(ordinal = 0, argsOnly = true) CommandSourceStack source) {
        return GameRuleData.get(source.getLevel().dimension()).orElse(original);
    }

    @Inject(method = "setRule", at = @At("RETURN"))
    private static <T extends GameRules.Value<T>> void invokeSave(CommandContext<CommandSourceStack> context, GameRules.Key<T> gameRule, CallbackInfoReturnable<Integer> cir) {
        GameRuleData.save(context.getSource().getServer());
    }
}
