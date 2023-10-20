package com.paul.extensions;

import com.paul.extensions.commands.CommandMore;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PaulXDaggerUtils.MOD_ID)
public class PaulXDaggerUtils {
    public static final String MOD_ID = "paulxdaggerutils";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
	System.out.println("Hello world!");
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
	ClientCommandHandler.instance.registerCommand(new CommandMore());
    }
}
