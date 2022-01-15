package com.eric0210.core.utils;

import com.eric0210.core.EricPackage;

import net.minecraft.server.v1_7_R4.MinecraftServer;

import org.bukkit.Bukkit;

public class TPSCalculator implements Runnable
{

	public static int TICK_COUNT;

	public static long[] TICKS = new long[600];

	public static long LAST_TICK;

	public static final TPSCalculator initialize()
	{
		final TPSCalculator instance = new TPSCalculator();
		Bukkit.getScheduler().runTaskTimer(EricPackage.instance(), instance, 0, 1);
		return instance;
	}

	public static double getTPS()
	{
		return getTPS(100);
	}

	public static double getTPS(final int ticks)
	{
		if (TICK_COUNT < ticks)
			return 20.0;
		try
		{
			final int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
			final long elapsed = System.currentTimeMillis() - TICKS[target];
			return ticks / (elapsed / 1000.0);
		}
		catch (final ArrayIndexOutOfBoundsException ignored)
		{
			return MinecraftServer.getServer().recentTps[0];
		}
	}

	public static long getElapsed(final int tickID)
	{
		final long time = TICKS[tickID % TICKS.length];
		return System.currentTimeMillis() - time;
	}

	@Override
	public void run()
	{
		TICKS[TICK_COUNT % TICKS.length] = System.currentTimeMillis();
		TICK_COUNT++;

		// Overflow failsafe
		if (TICK_COUNT >= Integer.MAX_VALUE - Integer.MAX_VALUE % TICKS.length)
			TICK_COUNT = 0;
	}
}
