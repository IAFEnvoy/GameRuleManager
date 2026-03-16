package com.iafenvoy.gamerule.util;

import com.iafenvoy.gamerule.GameRuleManager;
import net.minecraft.resources.ResourceLocation;

public final class RLUtil {
    //? forge {
    /*@SuppressWarnings("removal")
     *///?}
    public static ResourceLocation id(String id) {
        //? >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(GameRuleManager.MOD_ID, id);
        //?} else {
        /*return new ResourceLocation(GameRuleManager.MOD_ID, id);
         *///?}
    }

    //? forge {
    /*@SuppressWarnings("removal")
     *///?}
    public static ResourceLocation id(String namespace, String id) {
        //? >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(namespace, id);
        //?} else {
        /*return new ResourceLocation(namespace, id);
         *///?}
    }

    //? forge {
    /*@SuppressWarnings("removal")
     *///?}
    public static ResourceLocation tryParse(String id) {
        try {
            //? >=1.21 {
            return ResourceLocation.parse(id);
            //?} else {
            /*return new ResourceLocation(id);
             *///?}
        } catch (Exception e) {
            return null;
        }
    }
}
