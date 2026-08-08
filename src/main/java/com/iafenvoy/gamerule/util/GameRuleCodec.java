package com.iafenvoy.gamerule.util;

import com.mojang.serialization.Codec;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;

public final class GameRuleCodec {
    public static final Codec<GameRules> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING).xmap(m -> {
        GameRules gameRules = new GameRules(/*? >=1.21.2 {*/FeatureFlags.DEFAULT_FLAGS/*?}*/);
        gameRules.availableRules().forEach(rule -> applyRule(gameRules, rule, m.get(rule.id())));
        return gameRules;
    }, g -> {
        Map<String, String> map = new LinkedHashMap<>();
        g.availableRules().forEach(rule -> addRule(map, g, rule));
        return map;
    });

    private static <T> void applyRule(GameRules gameRules, GameRule<T> rule, String value) {
        if (value != null)
            rule.deserialize(value).resultOrPartial(message -> {}).ifPresent(parsed -> gameRules.set(rule, parsed, null));
    }

    private static <T> void addRule(Map<String, String> values, GameRules gameRules, GameRule<T> rule) {
        values.put(rule.id(), rule.serialize(gameRules.get(rule)));
    }
}
