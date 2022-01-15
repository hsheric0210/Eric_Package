package com.eric0210.StateScoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.barapi.SideBar;
import com.eric0210.core.api.barapi.SideBar.Entries;
import com.eric0210.core.utils.GameUtils.GetHandle;
import com.eric0210.core.utils.JavaHelper;
import com.eric0210.core.utils.TPSCalculator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public final class StatsBoard
{
	private final List<String> lines = new ArrayList<>();
	private final Player targetPlayer;
	private final Map<String, Object> replacements = new HashMap<>();
	private final SideBar theSideBar;

	StatsBoard(final Player targetPlayer)
	{
		this.targetPlayer = targetPlayer;
		theSideBar = new SideBar(this.targetPlayer);
		lines.add(ChatColor.GREEN + "Ping  %ping%ms");
		lines.add(ChatColor.GREEN + "TPS  %tps%");
		lines.add(ChatColor.LIGHT_PURPLE + "%totalmem%MB/%usingmem%MB");
		lines.add(ChatColor.LIGHT_PURPLE + "[ %memusinggraph% ]");
		lines.add(ChatColor.GOLD + "Memory");
		lines.add(" ");
		lines.add("%name%");

		theSideBar.updater.addExternalTask(() ->
		{
			final int ping = GetHandle.getPlayerHandle(this.targetPlayer).ping;
			final String pingstr = ping > 5000 ? "5000+" : (ping <= 50 ? ChatColor.AQUA : ping <= 150 ? ChatColor.GREEN : ping <= 250 ? ChatColor.YELLOW : ping <= 400 ? ChatColor.RED : ChatColor.DARK_RED).toString() + ping;
			replacements.put("%ping%", pingstr);
			final double realtps = TPSCalculator.getTPS();
			final String tps = (realtps >= 20.0 ? ChatColor.AQUA : realtps >= 18.0 ? ChatColor.GREEN : realtps >= 16.0 ? ChatColor.YELLOW : ChatColor.RED) + String.format("%.2f", realtps);
			replacements.put("%tps%", tps);

//			final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
//			final MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
//			final MemoryUsage nonHeap = memoryMXBean.getNonHeapMemoryUsage();

			final long freeMemoryKB = Math.round(Runtime.getRuntime().freeMemory() / 1024L);
			final long maxMemoryKB = Math.round(Runtime.getRuntime().maxMemory() / 1024L);
			final long totalMemoryKB = Math.round(Runtime.getRuntime().totalMemory() / 1024L);
			final long memoryInUseKB = totalMemoryKB - freeMemoryKB;
			replacements.put("%freemem%", freeMemoryKB / 1024L);
			replacements.put("%maxmem%", maxMemoryKB / 1024L);
			replacements.put("%totalmem%", totalMemoryKB / 1024L);
			replacements.put("%usingmem%", memoryInUseKB / 1024L);

			replacements.put("%memusinggraph%", drawMemoryUsageBar((int) totalMemoryKB, (int) memoryInUseKB));
			replacements.put("%name%", (this.targetPlayer.isOp() ? ChatColor.RED : ChatColor.RESET) + ChatColor.stripColor(this.targetPlayer.getName()));
		});

		createScoreboard(targetPlayer);
	}

	void setUpdaterRunning(final boolean state)
	{
		theSideBar.updater.setRunning(state);
	}

	private SideBar createScoreboard(final Player p)
	{
		try
		{
			final Entries e = theSideBar.getEntries(DisplaySlot.SIDEBAR, ChatColor.BOLD + "SERVER STATE");
			e.setReplacements(replacements);
			for (final String str : lines)
				e.setLine(lines.indexOf(str), str);
		}
		catch (final Throwable t)
		{
			EricPackage.logException("scoreboard creation", t);
		}

		return theSideBar;
	}

	public Player getPlayer()
	{
		return targetPlayer;
	}

	static String drawMemoryUsageBar(final int total, final int using)
	{
		final int standard = total / 10;
		return JavaHelper.drawProgressBar(total / standard, using / standard);
	}
}
