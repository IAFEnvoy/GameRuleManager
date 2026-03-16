package com.iafenvoy.gamerule.util;

import com.iafenvoy.gamerule.mixin.GameRules$RuleAccessor;
import com.mojang.serialization.Codec;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;

public final class GameRuleCodec {
    public static final Codec<GameRules> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING).xmap(m -> {
        GameRules gameRules = new GameRules(/*? >=1.21.2 {*/FeatureFlags.DEFAULT_FLAGS/*?}*/);
        /*? >=1.21.2 {*/
        gameRules/*?} else {*//*GameRules*//*?}*/.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            @Override
            public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                String name = key.getId();
                if (m.containsKey(name))
                    ((GameRules$RuleAccessor) gameRules.getRule(key)).gameRuleManager$deserialize(m.get(name));
            }
        });
        return gameRules;
    }, g -> {
        Map<String, String> map = new LinkedHashMap<>();
        /*? >=1.21.2 {*/
        g/*?} else {*//*GameRules*//*?}*/.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            @Override
            public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                map.put(key.getId(), g.getRule(key).serialize());
            }
        });
        return map;
    });
}
