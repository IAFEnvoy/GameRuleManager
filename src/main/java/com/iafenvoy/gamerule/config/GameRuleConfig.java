package com.iafenvoy.gamerule.config;

import com.google.gson.*;
import com.iafenvoy.gamerule.GameRuleManager;
import com.iafenvoy.gamerule.util.RLUtil;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public enum GameRuleConfig implements ResourceManagerReloadListener {
    INSTANCE;
    private static final String PATH = "./config/gamerule_manager.json";
    private static final String DEFAULT_KEY = "default";
    private static final ResourceKey<Level> DEFAULT = ResourceKey.create(Registries.DIMENSION, RLUtil.id(DEFAULT_KEY));
    private static final Map<ResourceKey<Level>, Map<String, ObjectBooleanPair<String>>> CONFIG = new HashMap<>();

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager) {
        loadConfig();
    }

    public static Map<String, ObjectBooleanPair<String>> getDefault() {
        if (CONFIG.isEmpty()) loadConfig();
        return CONFIG.getOrDefault(DEFAULT, new HashMap<>());
    }

    public static Map<String, ObjectBooleanPair<String>> get(ResourceKey<Level> level) {
        return CONFIG.getOrDefault(level, new HashMap<>());
    }

    public static void loadConfig() {
        try {
            FileInputStream stream = new FileInputStream(PATH);
            InputStreamReader reader = new InputStreamReader(stream);
            loadFromJsonObject(JsonParser.parseReader(reader).getAsJsonObject());
        } catch (FileNotFoundException e) {
            GameRuleManager.LOGGER.error("Failed to read file {}, creating new config file", PATH, e);
            try {
                FileUtils.write(new File(PATH), "{}", StandardCharsets.UTF_8);
            } catch (IOException ex) {
                GameRuleManager.LOGGER.error("Failed to write file {}", PATH, ex);
            }
        } catch (JsonIOException | JsonSyntaxException e) {
            GameRuleManager.LOGGER.error("Failed to read file {}", PATH, e);
        }
    }

    public static void loadFromJsonObject(JsonObject obj) {
        CONFIG.clear();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            ResourceLocation id = RLUtil.tryParse(entry.getKey());
            if (!(entry.getValue() instanceof JsonObject jsonObject) || id == null) continue;
            CONFIG.put(Objects.equals(entry.getKey(), DEFAULT_KEY) ? DEFAULT : ResourceKey.create(Registries.DIMENSION, id), parseForLevel(jsonObject));
        }
    }

    private static Map<String, ObjectBooleanPair<String>> parseForLevel(JsonObject obj) {
        Map<String, ObjectBooleanPair<String>> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            ObjectBooleanPair<String> pair = parseSingle(entry.getValue());
            if (pair != null) map.put(entry.getKey(), pair);
        }
        return map;
    }

    @Nullable
    private static ObjectBooleanPair<String> parseSingle(JsonElement element) {
        if (element instanceof JsonObject obj && obj.has("value")) {
            JsonElement lockElement = obj.get("lock");
            return new ObjectBooleanImmutablePair<>(obj.get("value").getAsString(), lockElement != null && lockElement.getAsBoolean());
        } else if (element instanceof JsonPrimitive primitive)
            return new ObjectBooleanImmutablePair<>(primitive.getAsString(), false);
        GameRuleManager.LOGGER.error("Cannot parse single ruler config: {}", element.toString());
        return null;
    }
}
