package com.paul.extensions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTP;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

@Mixin(CommandTP.class)
public abstract class MixinCommandTP extends CommandBase {
    @Overwrite
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	if (args.length < 1) {
	    throw new WrongUsageException("commands.tp.usage", new Object[0]);
	} else {
	    int i = 0;
	    Entity entity;

	    if (args.length != 2 && args.length != 4 && args.length != 6) {
		entity = getCommandSenderAsPlayer(sender);
	    } else {
		entity = getEntity(server, sender, args[0]);
		i = 1;
	    }

	    if (args.length != 1 && args.length != 2) {
		if (args.length < i + 3) {
		    throw new WrongUsageException("commands.tp.usage", new Object[0]);
		} else if (entity.world != null) {
		    int j = 4096;
		    int k = i + 1;
		    CommandBase.CoordinateArg commandbase$coordinatearg = parseCoordinate(entity.posX, args[i], true);
		    CommandBase.CoordinateArg commandbase$coordinatearg1 = parseCoordinate(entity.posY, args[k++],
			    -4096, 4096, false);
		    CommandBase.CoordinateArg commandbase$coordinatearg2 = parseCoordinate(entity.posZ, args[k++],
			    true);
		    CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate((double) entity.rotationYaw,
			    args.length > k ? args[k++] : "~", false);
		    CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate(
			    (double) entity.rotationPitch, args.length > k ? args[k] : "~", false);
		    teleportEntityToCoordinates(entity, commandbase$coordinatearg, commandbase$coordinatearg1,
			    commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4);
		    notifyCommandListener(sender, this, "commands.tp.success.coordinates",
			    new Object[] { entity.getName(), commandbase$coordinatearg.getResult(),
				    commandbase$coordinatearg1.getResult(), commandbase$coordinatearg2.getResult() });
		}
	    } else {
		Entity entity1 = getEntity(server, sender, args[args.length - 1]);

		if (entity1.world != entity.world) {
		    if (sender instanceof EntityPlayerMP && entity1 instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			if (net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, entity.dimension)) {
			    int prevDimension = player.dimension;
			    WorldServer worldserver1 = player.getServerWorld();
			    WorldServer worldserver2 = (WorldServer) entity1.world;
			    player.dimension = entity1.dimension;
			    player.connection.sendPacket(new SPacketRespawn(player.dimension,
				    worldserver2.getDifficulty(), worldserver2.getWorldInfo().getTerrainType(),
				    player.interactionManager.getGameType())); // Forge: Use new dimensions information
			    player.server.getPlayerList().updatePermissionLevel(player);
			    worldserver1.removeEntityDangerously(player);

			    player.isDead = false;
			    player.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw,
				    entity1.rotationPitch);

			    if (player.isEntityAlive()) {
				worldserver1.updateEntityWithOptionalForce(player, false);
				worldserver2.spawnEntity(player);
				worldserver2.updateEntityWithOptionalForce(player, false);
			    }

			    player.setWorld(worldserver2);
			    server.getPlayerList().preparePlayer(player, worldserver1);
			    player.setPositionAndUpdate(entity1.posX, entity1.posY, entity1.posZ);
			    player.interactionManager.setWorld(worldserver2);
			    server.getPlayerList().updateTimeAndWeatherForPlayer(player, worldserver2);
			    server.getPlayerList().syncPlayerInventory(player);
			    net.minecraftforge.fml.common.FMLCommonHandler.instance()
				    .firePlayerChangedDimensionEvent(player, prevDimension, player.dimension);
			}
		    }
		} else {
		    entity.dismountRidingEntity();

		    if (entity instanceof EntityPlayerMP) {
			((EntityPlayerMP) entity).connection.setPlayerLocation(entity1.posX, entity1.posY, entity1.posZ,
				entity1.rotationYaw, entity1.rotationPitch);
		    } else {
			entity.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw,
				entity1.rotationPitch);
		    }

		    notifyCommandListener(sender, this, "commands.tp.success",
			    new Object[] { entity.getName(), entity1.getName() });
		}
	    }
	}

    }

    @Shadow
    private static void teleportEntityToCoordinates(Entity teleportingEntity, CommandBase.CoordinateArg argX,
	    CommandBase.CoordinateArg argY, CommandBase.CoordinateArg argZ, CommandBase.CoordinateArg argYaw,
	    CommandBase.CoordinateArg argPitch) {
    }

}
