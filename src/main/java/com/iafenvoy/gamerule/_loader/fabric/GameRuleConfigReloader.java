package com.iafenvoy.gamerule._loader.fabric;

//? fabric && <=1.21.8 {
/*import com.iafenvoy.gamerule.config.GameRuleConfig;
import com.iafenvoy.gamerule.util.RLUtil;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class GameRuleConfigReloader implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return RLUtil.id("config_reload");
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        GameRuleConfig.INSTANCE.onResourceManagerReload(manager);
    }
}
*/