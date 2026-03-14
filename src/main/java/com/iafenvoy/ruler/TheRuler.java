package com.iafenvoy.ruler;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class TheRuler {
    public static final String MOD_ID = "the_ruler";
    public static final Logger LOGGER = LogUtils.getLogger();

    //? forge {
    /*@SuppressWarnings("removal")
     *///?}
    public static ResourceLocation id(String id) {
        //? >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
        //?} else {
        /*return new ResourceLocation(MOD_ID, id);
         *///?}
    }
}
