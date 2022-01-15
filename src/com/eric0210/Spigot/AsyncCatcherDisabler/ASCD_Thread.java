package com.eric0210.spigot.ASyncCatcherDisabler;

import org.spigotmc.AsyncCatcher;

import com.eric0210.core.EricPackage;

public class ASCD_Thread extends Thread
{
	private final EricPackage p;
	private Integer ScheduleID;
	private final int interval;
	private volatile boolean run;
	private int i;

	public ASCD_Thread(final EricPackage _p)
	{
		p = _p;
		interval = ASyncCatcherDisabler.self.getModuleConfig().get("Interval", 5000);
	}

	public void stopThread()
	{
		run = false;
	}

	@Override
	public void run()
	{
		if (p == null)
			return;

		currentThread().setName("org.spigot.AsyncCatcher Disabler Thread");
		EricPackage.info("'" + getName() + "' Thread Started.");

		while (run)
		{
			for (i = 0; i < interval; i += 1)
			{
				if (!run)
					return;
				try
				{
					sleep(1000L);
				}
				catch (final InterruptedException e)
				{
					EricPackage.logException("Could not sleep!", e);
				}
			}
			if (ScheduleID != null)
			{
				EricPackage.warning("The specified schedule already exists. Cancel scheduling...");
				continue;
			}
			ScheduleID = p.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable()
			{
				@Override
				public void run()
				{
					AsyncCatcher.enabled = false;
				}
			});
		}
	}
}
