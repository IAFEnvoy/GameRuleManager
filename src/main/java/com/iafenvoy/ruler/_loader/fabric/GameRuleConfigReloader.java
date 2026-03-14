package com.iafenvoy.ruler._loader.fabric;

//? fabric && <=1.21.8 {
/*import com.iafenvoy.ruler.GameRuleConfig;
import com.iafenvoy.ruler.TheRuler;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class GameRuleConfigReloader implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return TheRuler.id("config_reload");
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        GameRuleConfig.INSTANCE.onResourceManagerReload(manager);
    }
}
*/