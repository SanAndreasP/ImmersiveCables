/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = IWConstants.ID, name = IWConstants.NAME, version = IWConstants.VERSION, dependencies = IWConstants.DEPENDENCIES, guiFactory = ImmersiveWiring.GUI_FACTORY)
public class ImmersiveWiring
{
    static final String GUI_FACTORY = "de.sanandrew.mods.immersivewiring.client.util.IWGuiFactory";
    private static final String MOD_PROXY_CLIENT = "de.sanandrew.mods.immersivewiring.client.util.ClientProxy";
    private static final String MOD_PROXY_COMMON = "de.sanandrew.mods.immersivewiring.util.CommonProxy";

    @Mod.Instance(IWConstants.ID)
    public static ImmersiveWiring instance;
    @SidedProxy(modId = IWConstants.ID, clientSide = ImmersiveWiring.MOD_PROXY_CLIENT, serverSide = ImmersiveWiring.MOD_PROXY_COMMON)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        event.getModMetadata().autogenerated = false;

        IWConfiguration.initialize(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        IEFixes.postInit();
    }
}
