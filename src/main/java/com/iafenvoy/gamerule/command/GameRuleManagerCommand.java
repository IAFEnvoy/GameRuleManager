package com.iafenvoy.gamerule.command;

import com.iafenvoy.gamerule.config.GameRuleData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class GameRuleManagerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("gamerulemanager")
                .requires(source -> source.hasPermission(2))
                .then(literal("create").then(argument("dimension", DimensionArgument.dimension()).executes(GameRuleManagerCommand::create)))
                .then(literal("remove").then(argument("dimension", DimensionArgument.dimension()).executes(GameRuleManagerCommand::remove)))
                .then(literal("list").executes(GameRuleManagerCommand::list))
        );
    }

    private static int create(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        GameRuleData.create(source.getServer(), source.getLevel().dimension());
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        GameRuleData.remove(source.getServer(), source.getLevel().dimension());
        return 1;
    }

    private static int list(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSystemMessage(Component.literal("Current created for: " + GameRuleData.list().stream().map(ResourceKey::location).map(ResourceLocation::toString).collect(Collectors.joining(", "))));
        return 1;
    }
}
