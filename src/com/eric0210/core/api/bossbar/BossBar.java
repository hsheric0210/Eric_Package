package com.eric0210.core.api.bossbar;

import java.util.*;

import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.PacketUtils;

import net.minecraft.server.v1_7_R4.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BossBar extends TimerTask
{
	Timer updater;
	final EntityWither bossFront;
	final EntityWither bossBack;
	final Collection<Player> targetPlayers;
	String name;
	float healthProportion;

	public BossBar(final World world, final Collection<Player> targetPlayers, final int entityIDFront, final int entityIDBack)
	{
		this.targetPlayers = new HashSet<>(targetPlayers);
		this.targetPlayers.removeIf(player -> player.getWorld() != world);

		final WorldServer nmsWorld = GameUtils.GetHandle.getWorldHandle(world);

		bossFront = new EntityWither(nmsWorld);
		bossFront.d(entityIDFront);
		bossFront.setInvisible(true);
		bossFront.setLocation(0, 0, 0, 0, 0);

		bossBack = new EntityWither(nmsWorld);
		bossBack.d(entityIDBack);
		bossBack.setInvisible(true);
		bossBack.setLocation(0, 0, 0, 0, 0);
	}

	public final void show()
	{
		name = "";
		healthProportion = 1.0F;

		final Packet packetSpawnFront = new PacketPlayOutSpawnEntityLiving(bossFront);
		for (final Player target : targetPlayers)
			PacketUtils.sendPacket(target, packetSpawnFront);

		final Packet packetSpawnBack = new PacketPlayOutSpawnEntityLiving(bossBack);
		for (final Player target : targetPlayers)
			PacketUtils.sendPacket(target, packetSpawnBack);
	}

	/**
	 * @param autoUpdatePeriod
	 *                         - Auto-update period.
	 */
	public final void show(final long autoUpdatePeriod)
	{
		updater = new Timer("BossBar updater thread");
		updater.schedule(this, 0, autoUpdatePeriod);
		show();
	}

	/**
	 * @param message
	 *                new text of the BossBar
	 */
	public final void setText(final String message)
	{
		name = message;
	}

	/**
	 * @param proportion
	 *                   new health proportion of the BossBar
	 */
	public final void setHealthProportion(final float proportion)
	{
		healthProportion = proportion;
	}

	public final void hide()
	{
		if (updater != null)
			updater.cancel();
		final Packet packet = new PacketPlayOutEntityDestroy(bossFront.getId(), bossBack.getId());
		for (final Player target : targetPlayers)
			PacketUtils.sendPacket(target, packet);
	}

	/**
	 * @param name
	 *                         text of the BossBar
	 * @param healthProportion
	 *                         health proportion of the BossBar
	 */
	public final void update(final String name, final float healthProportion)
	{
		/* healthRate -> must be in range (0 ~ 1) */
		for (final Player target : targetPlayers)
		{
			final Location playerPos = target.getLocation();

			bossFront.setCustomName(name);
			bossFront.setHealth(bossFront.getMaxHealth() * healthProportion);

			bossBack.setCustomName(name);
			bossBack.setHealth(bossBack.getMaxHealth() * healthProportion);

			final Location bossFrontPos = playerPos.add(playerPos.getDirection().multiply(10));
			bossFront.setLocation(bossFrontPos.getX(), bossFrontPos.getY(), bossFrontPos.getZ(), 0.0F, 0.0F);
			PacketUtils.sendPacket(target, new PacketPlayOutEntityTeleport(bossFront));
			PacketUtils.sendPacket(target, new PacketPlayOutEntityMetadata(bossFront.getId(), bossFront.getDataWatcher(), false));

			final Location bossBackPos = playerPos.add(playerPos.getDirection().multiply(-10));
			bossBack.setLocation(bossBackPos.getX(), bossBackPos.getY(), bossBackPos.getZ(), 0.0F, 0.0F);
			PacketUtils.sendPacket(target, new PacketPlayOutEntityTeleport(bossBack));
			PacketUtils.sendPacket(target, new PacketPlayOutEntityMetadata(bossBack.getId(), bossBack.getDataWatcher(), false));
		}
	}

	@Override
	public void run()
	{
		update(name, healthProportion);
	}
}
