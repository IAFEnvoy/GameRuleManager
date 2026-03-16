package com.iafenvoy.gamerule._loader.fabric;

//? fabric {
/*import net.fabricmc.api.ModInitializer;
import net.minecraft.server.packs.PackType;
//? >=1.21.9 {
/^import com.iafenvoy.gamerule.config.GameRuleConfig;
import com.iafenvoy.gamerule.util.RLUtil;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
^///?} else {
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
//?}

public final class GameRuleManagerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        //? >=1.21.9 {
        /^ResourceLoader.get(PackType.SERVER_DATA).registerReloader(RLUtil.id("config_reload"), GameRuleConfig.INSTANCE);
        ^///?} else {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new GameRuleConfigReloader());
        //?}
    }
}
*/