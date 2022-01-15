package com.eric0210.core.api.item;

import com.eric0210.core.EricPackage;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import com.eric0210.core.api.EventHandler;

public class ItemSpawner
{
	ItemStack item;
	long spawnperiod;
	volatile boolean running;
	Location location;
	int schedule;

	public ItemSpawner(final Location loc, final ItemStack item, final long period)
	{
		Validate.notNull(loc);
		Validate.notNull(item);
		this.item = item;
		spawnperiod = period;
		location = loc;
	}

	public void StartSpawning()
	{
		running = true;

		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(EricPackage.instance(), () ->
		{
			if (item != null)
			{
				location.getWorld().dropItem(location, item);
				location.getWorld().playSound(location, Sound.ITEM_PICKUP, .8F, 1.0F);
			}
		}, 0L, spawnperiod);
	}

	public void StopSpawning()
	{
		running = false;
		Bukkit.getScheduler().cancelTask(schedule);
	}
}
