package com.iafenvoy.gamerule._loader.fabric;

import com.iafenvoy.gamerule.GameRuleManager;
import com.iafenvoy.gamerule.config.GameRuleConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public final class GameRuleManagerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(Identifier.fromNamespaceAndPath(GameRuleManager.MOD_ID, "config_reload"), GameRuleConfig.INSTANCE);
    }
}
