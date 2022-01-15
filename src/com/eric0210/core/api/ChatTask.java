package com.eric0210.core.api;

import com.eric0210.core.EricPackage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatTask
{
	private static volatile RunnableWithStringParameter task;

	public static void registerTask(final RunnableWithStringParameter task)
	{
		ChatTask.task = task;
	}

	public static boolean isPending()
	{
		return task != null;
	}

	// FIXME: Integrate in EventManager
	public static boolean onChat(final String msg)
	{
		if (task != null)
		{
			Bukkit.getScheduler().runTask(EricPackage.instance(), () ->
			{
				task.run(ChatColor.stripColor(msg));
				task = null;
			});
			return true;
		}
		return false;
	}
}
