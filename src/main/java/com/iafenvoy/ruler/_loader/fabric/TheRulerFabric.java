package com.iafenvoy.ruler._loader.fabric;

//? fabric {

/*import net.fabricmc.api.ModInitializer;
import net.minecraft.server.packs.PackType;
//? >=1.21.9 {
import com.iafenvoy.ruler.GameRuleConfig;
import com.iafenvoy.ruler.TheRuler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?} else {
/^import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
^///?}

public final class TheRulerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        //? >=1.21.9 {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(TheRuler.id("config_reload"), GameRuleConfig.INSTANCE);
        //?} else {
        /^ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new GameRuleConfigReloader());
        ^///?}
    }
}
*/