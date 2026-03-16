package com.iafenvoy.gamerule.config;

import com.google.gson.JsonParser;
import com.iafenvoy.gamerule.GameRuleManager;
import com.iafenvoy.gamerule.mixin.GameRules$RuleAccessor;
import com.iafenvoy.gamerule.mixin.LevelResourceAccessor;
import com.iafenvoy.gamerule.util.GameRuleCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameRuleData {
    private static final LevelResource PATH = LevelResourceAccessor.theRuler$newInstance("gamerule_manager.json");
    private static final Codec<Map<ResourceKey<Level>, GameRules>> CODEC = Codec.unboundedMap(ResourceKey.codec(Registries.DIMENSION), GameRuleCodec.CODEC);
    private static final Map<ResourceKey<Level>, GameRules> DATA = new HashMap<>();

    public static void load(MinecraftServer server) {
        DATA.clear();
        try {
            DATA.putAll(CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(FileUtils.readFileToString(server.getWorldPath(PATH).toFile(), StandardCharsets.UTF_8))).resultOrPartial(GameRuleManager.LOGGER::error).orElseThrow());
        } catch (FileNotFoundException e) {
            save(server);
        } catch (Exception e) {
           GameRuleManager.LOGGER.error("Failed to load config", e);
        }
    }

    public static void save(MinecraftServer server) {
        try {
            FileUtils.write(server.getWorldPath(PATH).toFile(), CODEC.encodeStart(JsonOps.INSTANCE, DATA).resultOrPartial(GameRuleManager.LOGGER::error).orElseThrow().toString(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            GameRuleManager.LOGGER.error("Failed to create config", ex);
        }
    }

    public static void create(ResourceKey<Level> level) {
        DATA.computeIfAbsent(level, GameRuleData::createEmpty);
    }

    public static void remove(ResourceKey<Level> level) {
        DATA.remove(level);
    }

    private static GameRules createEmpty(ResourceKey<Level> level) {
        GameRules gameRules = new GameRules(/*? >=1.21.2 {*/FeatureFlags.DEFAULT_FLAGS/*?}*/);
        Map<String, ObjectBooleanPair<String>> rules = GameRuleConfig.get(level);
        /*? >=1.21.2 {*/
        gameRules/*?} else {*//*GameRules*//*?}*/.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            @Override
            public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                String name = key.getId();
                if (rules.containsKey(name))
                    ((GameRules$RuleAccessor) gameRules.getRule(key)).theRuler$deserialize(rules.get(name).left());
            }
        });
        return gameRules;
    }

    public static Optional<GameRules> get(ResourceKey<Level> level) {
        return Optional.ofNullable(DATA.get(level));
    }
}
