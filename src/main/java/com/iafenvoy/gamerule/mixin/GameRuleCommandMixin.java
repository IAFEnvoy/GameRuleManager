package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import com.iafenvoy.server.i18n.ServerI18nExceptionType;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.GameRuleCommand;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRuleCommand.class)
public class GameRuleCommandMixin {
    @Inject(method = "setRule", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules$Value;setFromArgument(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)V"))
    private static <T extends GameRules.Value<T>> void onSetGameRule(CommandContext<CommandSourceStack> context, GameRules.Key<T> key, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        if (GameRuleData.isGameRuleLocked(source.getLevel().dimension(), key.getId()))
            throw new ServerI18nExceptionType("message.gamerule_manager.gamerule_locked").create(source);
    }

    @ModifyExpressionValue(method = "setRule", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getGameRules()Lnet/minecraft/world/level/GameRules;"))
    private static GameRules changeSetGameRules(GameRules original, @Local(ordinal = 0, argsOnly = true) CommandContext<CommandSourceStack> source) {
        return GameRuleData.get(source.getSource().getLevel().dimension()).map(GameRuleData.LevelDataEntry::getGameRules).orElse(original);
    }

    @ModifyExpressionValue(method = "queryRule", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getGameRules()Lnet/minecraft/world/level/GameRules;"))
    private static GameRules changeQueryGameRules(GameRules original, @Local(ordinal = 0, argsOnly = true) CommandSourceStack source) {
        return GameRuleData.get(source.getLevel().dimension()).map(GameRuleData.LevelDataEntry::getGameRules).orElse(original);
    }

    @Inject(method = "setRule", at = @At("RETURN"))
    private static <T extends GameRules.Value<T>> void invokeSave(CommandContext<CommandSourceStack> context, GameRules.Key<T> gameRule, CallbackInfoReturnable<Integer> cir) {
        GameRuleData.save(context.getSource().getServer());
    }
}
