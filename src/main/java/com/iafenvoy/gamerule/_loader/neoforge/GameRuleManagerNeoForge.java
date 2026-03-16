package com.iafenvoy.gamerule._loader.neoforge;

//? neoforge {
import com.iafenvoy.gamerule.config.GameRuleConfig;
import com.iafenvoy.gamerule.GameRuleManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
//? >=1.21.4 {
import com.iafenvoy.gamerule.util.RLUtil;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
//?} else {
/*import net.neoforged.neoforge.event.AddReloadListenerEvent;
*///?}
//? <=1.20.6 {
/*import net.neoforged.neoforge.common.NeoForge;
 *///?}

@Mod(GameRuleManager.MOD_ID)
@EventBusSubscriber/*? <=1.20.6 {*//*(bus = EventBusSubscriber.Bus.MOD)*//*?}*/
public class GameRuleManagerNeoForge {
    public GameRuleManagerNeoForge() {
        //? <=1.20.6 {
        /*NeoForge.EVENT_BUS.addListener(GameRuleManagerNeoForge::registerServerListener);
         *///?}
    }

    //? >=1.21 {
    @SubscribeEvent
            //?}
    public static void registerServerListener(/*? >=1.21.4 {*/AddServerReloadListenersEvent/*?} else {*//*AddReloadListenerEvent*//*?}*/ event) {
        event.addListener( /*? >=1.21.4 {*/RLUtil.id("config_reload"), /*?}*/GameRuleConfig.INSTANCE);
    }
}
