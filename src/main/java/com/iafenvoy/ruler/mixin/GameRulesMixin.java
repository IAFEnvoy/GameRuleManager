package com.iafenvoy.ruler.mixin;

import com.google.gson.JsonPrimitive;
import com.iafenvoy.ruler.GameRuleConfig;
import com.iafenvoy.ruler.TheRuler;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
public class GameRulesMixin {
    @Inject(method = "getRule", at = @At("TAIL"))
    private <T extends GameRules.Value<T>> void modifyGameRule(GameRules.Key<T> key, CallbackInfoReturnable<T> cir) {
        T original = cir.getReturnValue();
        JsonPrimitive primitive = GameRuleConfig.INSTANCE.getData(key.getId());
        if (primitive != null)
            try {
                ((GameRules$RuleAccessor) original).theRuler$deserialize(primitive.getAsString());
            } catch (Exception e) {
                TheRuler.LOGGER.error("Fail to set game rule {}", key.getId(), e);
            }
    }
}
