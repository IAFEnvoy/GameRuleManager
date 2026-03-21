package com.iafenvoy.gamerule.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.gamerule.GameRuleManager;
import com.iafenvoy.gamerule.mixin.GameRules$RuleAccessor;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.Nullable;

public enum GameRuleConfig implements ResourceManagerReloadListener {
    INSTANCE;
    private static final String DEFAULT_PATH = "./config/gamerule_manager/default.json";
    private static final String SPECIFIC_PATH = "./config/gamerule_manager/specific.json";
    private static final Codec<Map<ResourceLocation, LevelGameRuleConfig>> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, LevelGameRuleConfig.CODEC);
    private static LevelGameRuleConfig DEFAULT = new LevelGameRuleConfig(Map.of(), Optional.empty());
    private static final Map<ResourceLocation, LevelGameRuleConfig> SPECIFIC = new HashMap<>();
    private static final boolean INITIALIZED;

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager) {
        loadConfig();
    }

    static {
        loadConfig();
        INITIALIZED = true;
    }

    public static void loadConfig() {
        try {
            DEFAULT = LevelGameRuleConfig.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(new FileReader(DEFAULT_PATH))).resultOrPartial(GameRuleManager.LOGGER::error).orElseThrow();
        } catch (Exception e) {
            if (!INITIALIZED) GameRuleManager.LOGGER.error("Failed to read file {}", DEFAULT_PATH, e);
        }
        try {
            SPECIFIC.clear();
            SPECIFIC.putAll(CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(new FileReader(SPECIFIC_PATH))).resultOrPartial(GameRuleManager.LOGGER::error).orElseThrow());
        } catch (Exception e) {
            if (!INITIALIZED) GameRuleManager.LOGGER.error("Failed to read file {}", SPECIFIC_PATH, e);
        }
    }

    public static LevelGameRuleConfig getDefault() {
        return DEFAULT;
    }

    public static LevelGameRuleConfig get(ResourceKey<Level> level) {
        return DEFAULT.combine(SPECIFIC.get(level.location()));
    }

    public record LevelGameRuleConfig(Map<String, GameRuleEntry> gamerules, Optional<DifficultyEntry> difficulty) {
        public static final Codec<LevelGameRuleConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.unboundedMap(Codec.STRING, GameRuleEntry.DIRECT_OR_OBJECT).optionalFieldOf("gamerules", Map.of()).forGetter(LevelGameRuleConfig::gamerules),
                DifficultyEntry.DIRECT_OR_OBJECT.optionalFieldOf("difficulty").forGetter(LevelGameRuleConfig::difficulty)
        ).apply(i, LevelGameRuleConfig::new));

        public LevelGameRuleConfig combine(@Nullable LevelGameRuleConfig top) {
            if (top == null) return this;
            ImmutableMap.Builder<String, GameRuleEntry> builder = ImmutableMap.builder();
            builder.putAll(this.gamerules);
            builder.putAll(top.gamerules);
            return new LevelGameRuleConfig(builder.buildKeepingLast(), top.difficulty.isPresent() ? top.difficulty : this.difficulty);
        }

        public void apply(GameRules gameRules) {
            this.apply(gameRules, d -> {
            }, false);
        }

        public void apply(GameRules gameRules, Consumer<Difficulty> difficultySetter, boolean lockOnly) {
            /*? >=1.21.2 {*/
            gameRules/*?} else {*//*GameRules*//*?}*/.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                @Override
                public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                    String name = key.getId();
                    if (gamerules.containsKey(name) && (!lockOnly || gamerules.get(name).lock()))
                        ((GameRules$RuleAccessor) gameRules.getRule(key)).gameRuleManager$deserialize(gamerules.get(name).value());
                }
            });
            this.difficulty.filter(x -> x.lock).map(x -> x.value).ifPresent(difficultySetter);
        }
    }

    public record GameRuleEntry(String value, boolean lock) {
        public static final Codec<String> ANY_JSON_PRIMITIVE = ExtraCodecs.JSON.comapFlatMap(j -> j instanceof JsonPrimitive ? DataResult.success(j.toString()) : DataResult.error(() -> "Not a JsonPrimitive"), JsonPrimitive::new);
        public static final Codec<GameRuleEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                ANY_JSON_PRIMITIVE.fieldOf("value").forGetter(GameRuleEntry::value),
                Codec.BOOL.fieldOf("lock").forGetter(GameRuleEntry::lock)
        ).apply(i, GameRuleEntry::new));
        public static final Codec<GameRuleEntry> DIRECT_OR_OBJECT = Codec.either(ANY_JSON_PRIMITIVE, CODEC).xmap(e -> e.map(x -> new GameRuleEntry(x, false), Function.identity()), Either::right);
    }

    public record DifficultyEntry(Difficulty value, boolean lock) {
        public static final Codec<DifficultyEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                Difficulty.CODEC.fieldOf("value").forGetter(DifficultyEntry::value),
                Codec.BOOL.fieldOf("lock").forGetter(DifficultyEntry::lock)
        ).apply(i, DifficultyEntry::new));
        public static final Codec<DifficultyEntry> DIRECT_OR_OBJECT = Codec.either(Difficulty.CODEC, CODEC).xmap(e -> e.map(x -> new DifficultyEntry(x, false), Function.identity()), Either::right);
    }
}
