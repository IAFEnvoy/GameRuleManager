package com.iafenvoy.ruler._loader.neoforge;

//? neoforge {
import com.iafenvoy.ruler.GameRuleConfig;
import com.iafenvoy.ruler.TheRuler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
//? >=1.21.4 {
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
//?} else {
/*import net.neoforged.neoforge.event.AddReloadListenerEvent;
*///?}
import net.neoforged.fml.common.EventBusSubscriber;
//? <=1.20.6 {
/*import net.neoforged.neoforge.common.NeoForge;
 *///?}

@Mod(TheRuler.MOD_ID)
//? >=1.21 {
@EventBusSubscriber
//?} else {
/*@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
 *///?}
public class TheRulerNeoForge {
    public TheRulerNeoForge() {
        //? <=1.20.6 {
        /*NeoForge.EVENT_BUS.addListener(TheRulerNeoForge::registerServerListener);
         *///?}
    }

    //? >=1.21 {
    @SubscribeEvent
            //?}
    public static void registerServerListener(/*? >=1.21.4 {*/AddServerReloadListenersEvent/*?} else {*//*AddReloadListenerEvent*//*?}*/ event) {
        event.addListener( /*? >=1.21.4 {*/TheRuler.id("config_reload"), /*?}*/GameRuleConfig.INSTANCE);
    }
}
