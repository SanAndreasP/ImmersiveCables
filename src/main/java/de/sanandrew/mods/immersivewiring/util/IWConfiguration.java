/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.util;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IWConfiguration
{
    public static Configuration config;

    //region AE2
    public static final String CAT_AE2 = "Applied Energistics 2";

    public static boolean ae2Enabled = true;
    public static float ae2FluixRelayPowerDrain = 16.0F;
    public static float ae2DenseRelayPowerDrain = 16.0F;
    public static float ae2FluixTransformerPowerDrain = 16.0F;
    public static float ae2DenseTransformerPowerDrain = 16.0F;
    public static float ae2QuartzConnectorPowerDrain = 4.0F;
    public static int ae2FluixWireMaxLength = 24;
    public static int ae2DenseWireMaxLength = 16;
    public static int ae2QuartzWireMaxLength = 16;
    //endregion

    public static boolean isAe2Enabled() {
        return ae2Enabled && Loader.isModLoaded(IWConstants.COMPAT_APPLIEDENERGISTICS);
    }

    public static void initialize(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        synchronize();
    }

    public static void synchronize() {
        String desc;

        //region AE2
        {
            desc = "Enables or disables the Applied Energistics 2 features. Will be ignored if AE2 is not available.";
            ae2Enabled = config.getBoolean("enabled", CAT_AE2, ae2Enabled, desc);
            config.getCategory(CAT_AE2).get("enabled").setRequiresMcRestart(true);

            desc = "The power drained from the AE system by a regular fluix relay.";
            ae2FluixRelayPowerDrain = config.getFloat("fluixRelayPowerDrain", CAT_AE2, ae2FluixRelayPowerDrain, 0.0F, Float.MAX_VALUE, desc);

            desc = "The power drained from the AE system by a dense fluix relay.";
            ae2DenseRelayPowerDrain = config.getFloat("denseRelayPowerDrain", CAT_AE2, ae2DenseRelayPowerDrain, 0.0F, Float.MAX_VALUE, desc);

            desc = "The power drained from the AE system by a regular fluix transformer.";
            ae2FluixTransformerPowerDrain = config.getFloat("fluixTransformerPowerDrain", CAT_AE2, ae2FluixTransformerPowerDrain, 0.0F, Float.MAX_VALUE, desc);

            desc = "The power drained from the AE system by a dense fluix transformer.";
            ae2DenseTransformerPowerDrain = config.getFloat("denseTransformerPowerDrain", CAT_AE2, ae2DenseTransformerPowerDrain, 0.0F, Float.MAX_VALUE, desc);

            desc = "The maximum length in blocks a regular fluix wire can be.";
            ae2FluixWireMaxLength = config.getInt("fluixWireMaxLength", CAT_AE2, ae2FluixWireMaxLength, 0, Integer.MAX_VALUE, desc);

            desc = "The maximum length in blocks a dense fluix wire can be.";
            ae2DenseWireMaxLength = config.getInt("denseWireMaxLength", CAT_AE2, ae2DenseWireMaxLength, 0, Integer.MAX_VALUE, desc);
        }
        //endregion

        if( config.hasChanged() ) {
            config.save();
        }
    }

    public static ConfigCategory getCategory(String category) {
        return config.getCategory(category);
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if( event.getModID().equals(IWConstants.ID) ) {
            synchronize();
        }
    }
}
