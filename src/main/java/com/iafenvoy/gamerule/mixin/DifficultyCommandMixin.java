package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import com.iafenvoy.server.i18n.ServerI18nExceptionType;
import com.llamalad7.mixinextras.injector./*? !forge {*/v2./*?}*/WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.DifficultyCommand;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DifficultyCommand.class)
public class DifficultyCommandMixin {
    @Inject(method = "setDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setDifficulty(Lnet/minecraft/world/Difficulty;Z)V"))
    private static void onSetGameRule(CommandSourceStack source, Difficulty difficulty, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        ResourceKey<Level> key = source.getLevel().dimension();
        if (GameRuleData.isDifficultyLocked(key))
            throw new ServerI18nExceptionType("message.gamerule_manager.difficulty_locked").create(source);
        else GameRuleData.get(key).ifPresent(x -> x.setDifficulty(difficulty));
    }

    @WrapWithCondition(method = "setDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setDifficulty(Lnet/minecraft/world/Difficulty;Z)V"))
    private static boolean changeSetGameRules(MinecraftServer instance, Difficulty difficulty, boolean forced, @Local(ordinal = 0, argsOnly = true) CommandSourceStack source) {
        return GameRuleData.get(source.getLevel().dimension()).isPresent();
    }

    @Inject(method = "setDifficulty", at = @At("RETURN"))
    private static void invokeSave(CommandSourceStack source, Difficulty difficulty, CallbackInfoReturnable<Integer> cir) {
        GameRuleData.save(source.getServer());
    }
}
