package com.iafenvoy.ruler.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.Value.class)
public interface GameRules$RuleAccessor {
    @Invoker("deserialize")
    void theRuler$deserialize(String value);
}
