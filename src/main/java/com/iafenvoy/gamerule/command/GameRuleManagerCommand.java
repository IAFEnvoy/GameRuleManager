package com.iafenvoy.gamerule.command;

import com.iafenvoy.gamerule.config.GameRuleData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class GameRuleManagerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("gamerulemanager")
                .then(literal("create").then(argument("dimension", DimensionArgument.dimension()).executes(GameRuleManagerCommand::create)))
                .then(literal("remove").then(argument("dimension", DimensionArgument.dimension()).executes(GameRuleManagerCommand::remove)))
                .then(literal("list")
                )
        );
    }

    private static int create(CommandContext<CommandSourceStack> ctx) {
        GameRuleData.create(ctx.getSource().getLevel().dimension());
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> ctx) {
        GameRuleData.remove(ctx.getSource().getLevel().dimension());
        return 1;
    }
}
