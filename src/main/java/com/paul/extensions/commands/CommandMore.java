package com.paul.extensions.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ChatColorEntry;

public class CommandMore extends CommandBase {

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return "more";
    }

    @Override
    public String getUsage(ICommandSender sender) {
	// TODO Auto-generated method stub
	return "/more";
    }
    @Override
    public int getRequiredPermissionLevel() {
        // TODO Auto-generated method stub
        return 2;
    }
    

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	if (!(sender instanceof EntityPlayer)) {
	    sender.sendMessage(new TextComponentString("not entityliving"));
	    return;
	}
	EntityPlayer entity = (EntityPlayer) sender;
	
	ItemStack item;
	if ((item = entity.getHeldItemMainhand()).isEmpty()) {
	    sender.sendMessage(new TextComponentString("empty"));
	    return;
	
	}
	int max = item.getMaxStackSize();
	entity.getHeldItemMainhand().setCount(max);
	entity.sendMessage(new TextComponentString(
		TextFormatting.GOLD + "Here you go..."));
	// TODO Auto-generated method stub
	
    }

}
