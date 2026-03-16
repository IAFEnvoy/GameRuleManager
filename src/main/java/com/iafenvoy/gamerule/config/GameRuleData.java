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
import java.util.*;

public class GameRuleData {
    private static final LevelResource PATH = LevelResourceAccessor.gameRuleManager$newInstance("gamerule_manager.json");
    private static final Codec<Map<ResourceKey<Level>, GameRules>> CODEC = Codec.unboundedMap(ResourceKey.codec(Registries.DIMENSION), GameRuleCodec.CODEC);
    private static final Map<ResourceKey<Level>, GameRules> DATA = new HashMap<>();

    public static void load(MinecraftServer server) {
        DATA.clear();
        try {
            DATA.putAll(CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(FileUtils.readFileToString(server.getWorldPath(PATH).toFile(), StandardCharsets.UTF_8))).resultOrPartial(GameRuleManager.LOGGER::error).orElseThrow());
        } catch (FileNotFoundException e) {
            save(server);
        } catch (Exception e) {
            GameRuleManager.LOGGER.error("Failed to load world config", e);
        }
    }

    public static void save(MinecraftServer server) {
        try {
            FileUtils.write(server.getWorldPath(PATH).toFile(), CODEC.encodeStart(JsonOps.INSTANCE, DATA).resultOrPartial(GameRuleManager.LOGGER::error).orElseThrow().toString(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            GameRuleManager.LOGGER.error("Failed to create world config", ex);
        }
    }

    public static void forceSetLockRules(MinecraftServer server) {
        GameRuleManager.LOGGER.info("Trying to force set all locked gamerules");
        apply(server.getGameRules(), GameRuleConfig.getDefault(), true);
        for (Map.Entry<ResourceKey<Level>, GameRules> entry : DATA.entrySet())
            apply(entry.getValue(), GameRuleConfig.get(entry.getKey()), true);
    }

    public static void create(MinecraftServer server, ResourceKey<Level> level) {
        DATA.computeIfAbsent(level, GameRuleData::createEmpty);
        save(server);
    }

    public static void remove(MinecraftServer server, ResourceKey<Level> level) {
        DATA.remove(level);
        save(server);
    }

    private static GameRules createEmpty(ResourceKey<Level> level) {
        GameRules gameRules = new GameRules(/*? >=1.21.2 {*/FeatureFlags.DEFAULT_FLAGS/*?}*/);
        apply(gameRules, GameRuleConfig.get(level), false);
        return gameRules;
    }

    private static void apply(GameRules gameRules, Map<String, ObjectBooleanPair<String>> rules, boolean lockOnly) {
        /*? >=1.21.2 {*/
        gameRules/*?} else {*//*GameRules*//*?}*/.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            @Override
            public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                String name = key.getId();
                if (rules.containsKey(name) && (!lockOnly || rules.get(name).rightBoolean()))
                    ((GameRules$RuleAccessor) gameRules.getRule(key)).gameRuleManager$deserialize(rules.get(name).left());
            }
        });
    }

    public static Optional<GameRules> get(ResourceKey<Level> level) {
        return Optional.ofNullable(DATA.get(level));
    }

    public static Collection<ResourceKey<Level>> list() {
        return DATA.keySet();
    }
}
