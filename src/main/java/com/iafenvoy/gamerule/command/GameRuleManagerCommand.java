package com.iafenvoy.gamerule.command;

import com.iafenvoy.gamerule.config.GameRuleData;
import com.iafenvoy.server.i18n.ServerI18n;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;

import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class GameRuleManagerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("gamerulemanager")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(literal("create").then(argument("dimension", DimensionArgument.dimension()).executes(GameRuleManagerCommand::create)))
                .then(literal("remove").then(argument("dimension", DimensionArgument.dimension()).executes(GameRuleManagerCommand::remove)))
                .then(literal("list").executes(GameRuleManagerCommand::list))
        );
    }

    private static int create(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = DimensionArgument.getDimension(ctx, "dimension");
        GameRuleData.create(source.getServer(), level);
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = DimensionArgument.getDimension(ctx, "dimension");
        GameRuleData.remove(source.getServer(), level);
        return 1;
    }

    private static int list(CommandContext<CommandSourceStack> ctx) {
        ServerI18n.sendMessage(ctx.getSource(), "message.gamerule_manager.list", GameRuleData.list().stream().map(ResourceKey::identifier).map(Identifier::toString).collect(Collectors.joining(", ")));
        return 1;
    }
}
