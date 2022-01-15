package com.eric0210.spigot.ASyncCatcherDisabler;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;

public class ASyncCatcherDisabler extends Module
{
	public static ASyncCatcherDisabler self;
	private ASCD_Thread ascd_thread;

	@Override
	public void load()
	{
		self = this;
		if (!Bukkit.getVersion().contains("Spigot"))
			EricPackage.info("This server is not using the Spigot. Disabling AsyncCatcherDisabler...");
		ascd_thread = new ASCD_Thread(EricPackage.instance());
		if (!ascd_thread.isAlive())
		{
			ascd_thread.start();
			EricPackage.info("AsyncCatcher Disabler thread started");
		}
	}

	@Override
	public void unload()
	{
		if (ascd_thread != null)
		{
			EricPackage.info("AsyncCatcher Disabler thread stopped");
			ascd_thread.stopThread();
		}
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		return false;
	}
}
