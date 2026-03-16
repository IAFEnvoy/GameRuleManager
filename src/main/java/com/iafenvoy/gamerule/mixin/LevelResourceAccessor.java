package com.iafenvoy.gamerule.mixin;

import net.minecraft.world.level.storage.LevelResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelResource.class)
public interface LevelResourceAccessor {
    @Invoker("<init>")
    static LevelResource gameRuleManager$newInstance(String relativePath) {
        throw new AssertionError("This method should be replaced by Mixin.");
    }
}
