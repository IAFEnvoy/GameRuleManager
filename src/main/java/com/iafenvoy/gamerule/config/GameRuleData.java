package com.iafenvoy.gamerule.config;

import com.google.gson.JsonParser;
import com.iafenvoy.gamerule.GameRuleManager;
import com.iafenvoy.gamerule.mixin.LevelResourceAccessor;
import com.iafenvoy.gamerule.util.GameRuleCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class GameRuleData {
    private static final LevelResource PATH = LevelResourceAccessor.gameRuleManager$newInstance("gamerule_manager.json");
    private static final Codec<Map<ResourceKey<Level>, LevelDataEntry>> CODEC = Codec.unboundedMap(ResourceKey.codec(Registries.DIMENSION), LevelDataEntry.CODEC);
    private static final Map<ResourceKey<Level>, LevelDataEntry> DATA = new HashMap<>();

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
        GameRuleManager.LOGGER.info("Trying to lock set all locked gamerules and difficulty");
        GameRuleConfig.getDefault().apply(server.getGameRules(), d -> server.setDifficulty(d, false), true);
        for (Map.Entry<ResourceKey<Level>, LevelDataEntry> entry : DATA.entrySet())
            GameRuleConfig.get(entry.getKey()).apply(entry.getValue().getGameRules(), entry.getValue()::setDifficulty, true);
    }

    public static void create(MinecraftServer server, ServerLevel level) {
        LevelDataEntry entry = DATA.computeIfAbsent(level.dimension(), l -> createEmpty(l, server.getWorldData().getDifficulty()));
        GameRuleConfig.get(level.dimension()).apply(level.getGameRules(), entry::setDifficulty, true);
        save(server);
    }

    public static void remove(MinecraftServer server, ServerLevel level) {
        DATA.remove(level.dimension());
        save(server);
    }

    private static LevelDataEntry createEmpty(ResourceKey<Level> level, Difficulty difficulty) {
        GameRules gameRules = new GameRules(/*? >=1.21.2 {*/FeatureFlags.DEFAULT_FLAGS/*?}*/);
        GameRuleConfig.get(level).apply(gameRules);
        return new LevelDataEntry(gameRules, difficulty);
    }

    public static Optional<LevelDataEntry> get(ResourceKey<Level> level) {
        return Optional.ofNullable(DATA.get(level));
    }

    public static Collection<ResourceKey<Level>> list() {
        return DATA.keySet();
    }

    public static Optional<GameRuleConfig.GameRuleEntry> getSingleGameRule(ResourceKey<Level> level, String key) {
        return DATA.containsKey(level) ? Optional.ofNullable(GameRuleConfig.get(level)).map(GameRuleConfig.LevelGameRuleConfig::gamerules).map(x -> x.get(key)) : Optional.empty();
    }

    public static Optional<GameRuleConfig.DifficultyEntry> getDifficulty(ResourceKey<Level> level) {
        return DATA.containsKey(level) ? Optional.ofNullable(GameRuleConfig.get(level)).map(GameRuleConfig.LevelGameRuleConfig::difficulty).flatMap(Function.identity()) : Optional.empty();
    }

    public static boolean isGameRuleLocked(ResourceKey<Level> level, String key) {
        return getSingleGameRule(level, key).map(GameRuleConfig.GameRuleEntry::lock).orElse(false);
    }

    public static boolean isDifficultyLocked(ResourceKey<Level> level) {
        return getDifficulty(level).map(GameRuleConfig.DifficultyEntry::lock).orElse(false);
    }

    public static final class LevelDataEntry {
        public static final Codec<LevelDataEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                GameRuleCodec.CODEC.fieldOf("gamerules").forGetter(LevelDataEntry::getGameRules),
                Difficulty.CODEC.fieldOf("difficulty").forGetter(LevelDataEntry::getDifficulty)
        ).apply(i, LevelDataEntry::new));
        private final GameRules gameRules;
        private Difficulty difficulty;

        public LevelDataEntry(GameRules gameRules, Difficulty difficulty) {
            this.gameRules = gameRules;
            this.difficulty = difficulty;
        }

        public GameRules getGameRules() {
            return gameRules;
        }

        public Difficulty getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }
    }
}
