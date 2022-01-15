package com.eric0210.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.IntStream;

import com.eric0210.core.EricPackage;

import net.minecraft.server.v1_7_R4.DedicatedPlayerList;
import net.minecraft.server.v1_7_R4.EntityFallingBlock;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftSound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.material.MaterialData;
import org.bukkit.util.BlockIterator;

public final class GameUtils
{
	/* Entity & Player related Common Methods */

	public static void applyVelocityTowards(final Entity entity, final Location towards, final int multiplier)
	{
		entity.setVelocity(entity.getVelocity().add(towards.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(multiplier)));
	}

	public static Block[] getBlocksInLineOfSight(final LivingEntity entity)
	{
		final int maxDist = 120;
		final int maxLength = 1;
		final ArrayList<Block> blocks = new ArrayList<>(maxLength + 1);

		final BlockIterator blockItr = new BlockIterator(entity, maxDist);
		while (blockItr.hasNext())
		{
			final Block next = blockItr.next();
			blocks.add(next);
			if (blocks.size() > maxLength)
				blocks.remove(0);

			if (next.getType() != Material.AIR)
				break;
		}
		return blocks.toArray(EMPTY_BLOCK_ARRAY);
	}

	public static Block getTargetBlock(final LivingEntity entity)
	{
		final int maxDist = 120;
		final int maxLength = 1;
		final ArrayList<Block> blocks = new ArrayList<>(maxLength + 1);

		final BlockIterator blockItr = new BlockIterator(entity, maxDist);
		while (blockItr.hasNext())
		{
			final Block next = blockItr.next();
			blocks.add(next);
			if (blocks.size() > maxLength)
				blocks.remove(0);

			if (next.getType() != Material.AIR)
				break;
		}
		return blocks.get(0);
	}

	public static boolean isPlayer(final Object anything)
	{
		return anything instanceof Player;
	}

	public static void sendPluginMessage(final CommandSender messagesender, final String message)
	{
		messagesender.sendMessage(EricPackage.PLUGIN_MESSAGE_FORMAT + ChatColor.RESET + message);
	}

	public static FallingBlock spawnFallingBlock(final World world, final Location position, final Material material, final byte data)
	{
		Validate.notNull(world, "World cannot be null");
		Validate.notNull(position, "Position cannot be null");
		Validate.notNull(material, "Material Type cannot be null");

		final EntityFallingBlock fallingBlock = new EntityFallingBlock(GetHandle.getWorldHandle(world), position.getX(), position.getY(), position.getZ(), net.minecraft.server.v1_7_R4.Block.getById(material.getId()), data);
		fallingBlock.ticksLived = 1;
		GetHandle.getWorldHandle(world).addEntity(fallingBlock);
		return (FallingBlock) fallingBlock.getBukkitEntity();
	}

	public static double distanceBetween(final Block block, final Block other)
	{
		return block.getLocation().distance(other.getLocation());
	}

	public static Entity[] findInSphere(final Location position, final double radius)
	{
		return position.getWorld().getEntities().stream().filter(ent -> ent.getLocation().distance(position) <= radius).toArray(Entity[]::new);
	}

	/* Sounds Common Methods */
	public static void playSound(final Location position, final String sound, final float volume, final float pitch)
	{
		GetHandle.getWorldHandle(position.getWorld()).makeSound(position.getX(), position.getY(), position.getZ(), sound, volume, pitch);
	}

	public static void playSound(final Location position, final Sound sound, final float volume, final float pitch)
	{
		playSound(position, CraftSound.getSound(sound), volume, pitch);
	}

	/* Effects & Particles Common Methods */
	public static void blockBreakEffect(final Location position, final Material block, final int loopCount, final double radius)
	{
		if (block.isBlock())
			sphereEffect(position, Effect.TILE_BREAK, new MaterialData(block), loopCount, radius);
	}

	public static <T> void sphereEffect(final Location position, final Effect effect, final T data, final int loopCount, final double radius)
	{
		final double x = position.getX();
		final double y = position.getY();
		final double z = position.getZ();
		IntStream.range(0, JavaHelper.random(loopCount - 5, loopCount + 5)).forEach(loop ->
		{
			final double effectX = JavaHelper.random(x - radius, x + radius);
			final double effectY = JavaHelper.random(y - radius, y + radius);
			final double effectZ = JavaHelper.random(z - radius, z + radius);
			final Location partialPosition = position.clone();
			partialPosition.setX(effectX);
			partialPosition.setY(effectY);
			partialPosition.setZ(effectZ);
			position.getWorld().playEffect(partialPosition, effect, data, 64);
		});
	}

	@SuppressWarnings("unchecked")
	public static Collection<Player> getOnlinePlayers()
	{
		try
		{
			final Field playerView = CraftServer.class.getDeclaredField("playerView");
			playerView.setAccessible(true);
			return (Collection<Player>) playerView.get(Bukkit.getServer());
		}
		catch (final NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Player[] getOnlinePlayersArray()
	{
		return Objects.requireNonNull(getOnlinePlayers()).toArray(EMPTY_PLAYER_ARRAY);
	}

	public static final class AmbigousMethods
	{
		public static int getHealthInt(final Damageable ent)
		{
			// noinspection RedundantCast - because of eclipse
			return (int) ent.getHealth();
		}

		public static double getHealthDouble(final Damageable ent)
		{
			return ent.getHealth();
		}

		public static int getMaxHealthInt(final Damageable ent)
		{
			// noinspection RedundantCast - because of eclipse
			return (int) ent.getMaxHealth();
		}

		public static double getMaxHealthDouble(final Damageable ent)
		{
			return ent.getMaxHealth();
		}

		private AmbigousMethods()
		{
		}
	}

	public static final class GetHandle
	{
		/**
		 * An inline method of ((CraftServer) server).getHandle()
		 * 
		 * @param  server
		 *                The bukkit server. Can get by Bukkit.getServer()
		 * @return        (DedicatedPlayerList) ((CraftServer) server).getHandle()
		 */
		public static DedicatedPlayerList getServerHandle(final Server server)
		{
			return ((CraftServer) server).getHandle();
		}

		/**
		 * An inline method of ((CraftPlayer) player).getHandle()
		 * 
		 * @param  player
		 *                The bukkit player
		 * @return        (EntityPlayer) ((CraftPlayer) player).getHandle()
		 */
		public static EntityPlayer getPlayerHandle(final Player player)
		{
			return ((CraftPlayer) player).getHandle();
		}

		/**
		 * An inline method of ((CraftWorld) world).getHandle()
		 * 
		 * @param  world
		 *               The bukkit world
		 * @return       (WorldServer) ((CraftWorld) world).getHandle()
		 */
		public static WorldServer getWorldHandle(final World world)
		{
			return ((CraftWorld) world).getHandle();
		}

		private GetHandle()
		{
		}
	}

	private static final Block[] EMPTY_BLOCK_ARRAY = new Block[0];
	private static final Player[] EMPTY_PLAYER_ARRAY = new Player[0];

	private GameUtils()
	{
	}
}
