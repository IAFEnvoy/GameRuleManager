package com.iafenvoy.gamerule._loader.neoforge;

import com.iafenvoy.gamerule.GameRuleManager;
import com.iafenvoy.gamerule.config.GameRuleConfig;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@Mod(GameRuleManager.MOD_ID)
@EventBusSubscriber
public final class GameRuleManagerNeoForge {
    @SubscribeEvent
    public static void registerServerListener(AddServerReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(GameRuleManager.MOD_ID, "config_reload"), GameRuleConfig.INSTANCE);
    }
}
