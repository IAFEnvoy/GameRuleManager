package com.iafenvoy.gamerule.mixin;

import com.iafenvoy.gamerule.config.GameRuleData;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
//? >=1.21.2 {
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
/*import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.Supplier;
*///?}

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, /*? <=1.21.1 {*//*Supplier<ProfilerFiller> profiler, *//*?}*/boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, /*? <=1.21.1 {*//*profiler, *//*?}*/isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    //? >=1.21.2 {
    @Inject(method = "getGameRules", at = @At("HEAD"), cancellable = true)
    private void modifyLevelGameRules(CallbackInfoReturnable<GameRules> cir) {
        GameRuleData.get(this.dimension()).map(GameRuleData.LevelDataEntry::getGameRules).ifPresent(cir::setReturnValue);
    }
    //?} else {
//    @Override
//    public GameRules getGameRules(){
//        return GameRuleData.get(this.dimension()).map(GameRuleData.LevelDataEntry::getGameRules).orElse(super.getGameRules());
//    }
    //?}

    @Override
    public @NotNull Difficulty getDifficulty() {
        return GameRuleData.get(this.dimension()).map(GameRuleData.LevelDataEntry::getDifficulty).orElse(super.getDifficulty());
    }
}
