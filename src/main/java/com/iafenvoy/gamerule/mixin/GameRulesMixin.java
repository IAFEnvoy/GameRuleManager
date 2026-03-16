package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleConfig;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? <=1.21.1 {
/*import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
*///?}
import java.util.Map;

@Mixin(GameRules.class)
public class GameRulesMixin {
    //? <=1.21.1 {
    /*@Shadow
    @Final
    private Map<GameRules.Key<?>, GameRules.Value<?>> rules;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void setDefaultRules1(CallbackInfo ci) {
        this.setDefaultRules(this.rules, ci);
    }
    *///?}

    @Inject(method = /*? >=1.21.2 {*/"<init>(Ljava/util/Map;Lnet/minecraft/world/flag/FeatureFlagSet;)V"/*?} else {*//*"<init>(Ljava/util/Map;)V"*//*?}*/, at = @At("RETURN"))
    private void setDefaultRules(Map<GameRules.Key<?>, GameRules.Value<?>> rules, /*? >=1.21.2 {*/FeatureFlagSet enabledFeatures, /*?}*/CallbackInfo ci) {
        //We will only apply default ones here, others will be applied in GameRuleManager
        Map<String, ObjectBooleanPair<String>> defaults = GameRuleConfig.getDefault();
        for (Map.Entry<GameRules.Key<?>, GameRules.Value<?>> entry : rules.entrySet())
            if (defaults.containsKey(entry.getKey().getId()))
                ((GameRules$RuleAccessor) entry.getValue()).theRuler$deserialize(defaults.get(entry.getKey().getId()).left());
    }
}
