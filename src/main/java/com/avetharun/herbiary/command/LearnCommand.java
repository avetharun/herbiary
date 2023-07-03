package com.avetharun.herbiary.command;
import com.avetharun.herbiary.Items.UnlockableNamedItem;
import com.avetharun.herbiary.hUtil.alib;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class LearnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(CommandManager.literal("forget").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(access))
                                .suggests((context, builder) -> suggestItems(context.getSource(), builder))
                                .executes(context -> executeForget(context.getSource(), EntityArgumentType.getPlayer(context, "player"), ItemStackArgumentType.getItemStackArgument(context, "item"))))));
        dispatcher.register(CommandManager.literal("learn").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(access))
                                .suggests((context, builder) -> suggestItems(context.getSource(), builder))
                                .executes(context -> executeLearn(context.getSource(), EntityArgumentType.getPlayer(context, "player"), ItemStackArgumentType.getItemStackArgument(context, "item"), true)))));
    }

    private static CompletableFuture<Suggestions> suggestItems(ServerCommandSource source, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(Registries.ITEM.stream().map(Registries.ITEM::getId), builder);
    }

    private static int executeLearn(ServerCommandSource source, PlayerEntity player, ItemStackArgument Astack, boolean forceLearn) {
        ItemStack stack = Astack.getItem().getDefaultStack();
        if (forceLearn) {
            if (!(stack == null) && !stack.isEmpty()) {
                UnlockableNamedItem.Unlock((ServerPlayerEntity) player, false, stack);
                source.sendFeedback(() -> player.getDisplayName().copy().append(" has learned " + stack.getTranslationKey()), true);
                return 1;
            } else {
                source.sendError(Text.of("Invalid item."));
                return 0;
            }
        } else {
            if (!(stack == null) && !stack.isEmpty()) {
                if (alib.playerHasItem((ServerPlayerEntity) player, stack)) {
                    UnlockableNamedItem.Unlock((ServerPlayerEntity) player, false, stack);
                    return 1;
                }
            } else {
                source.sendError(Text.of("Invalid item."));
                return 0;
            }

        }
        return 0;
    }

    private static int executeForget(ServerCommandSource source, PlayerEntity player, ItemStackArgument Astack) {
        ItemStack stack = Astack.getItem().getDefaultStack();
        if (!(stack == null) && !stack.isEmpty()) {
            UnlockableNamedItem.Unlock((ServerPlayerEntity) player, true, stack);
            source.sendFeedback(()->player.getDisplayName().copy().append(" has forgotten " + stack.getTranslationKey()), true);
            return 1;
        } else {
            source.sendError(Text.of("Invalid item."));
            return 0;
        }
    }
}