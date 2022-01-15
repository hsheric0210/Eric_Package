package com.eric0210.StructureGenerater;

import java.util.Locale;
import java.util.StringJoiner;

import com.eric0210.StructureGenerater.Generater.StructureType;
import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandManager;
import com.eric0210.core.utils.GameUtils;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class StructureGenerater extends Module
{
	@Override
	public void load()
	{
		CommandManager.registerHandler(this, "create");
	}

	@Override
	public void unload()
	{
		CommandManager.unregisterHandler("create");
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		return false;
	}

	@Override
	public boolean onCommand(final CommandSender s, final String l, final String[] d)
	{
		if (l.equalsIgnoreCase("create"))
			if (s instanceof Player)
			{
				final Player p = (Player) s;
				if (d.length > 0)
				{
					final String arg = d[0];
					final Block targetPos = GameUtils.getTargetBlock(p);
					final StructureType type = Generater.toStructureType(arg);
					if (type != null)
						try
						{
							final int structureSize = d.length > 1 ? Integer.parseInt(d[1]) : 0;
							type.generate(targetPos.getLocation(), structureSize);
						}
						catch (final NumberFormatException ex)
						{
							s.sendMessage(ChatColor.RED + "Unexpected structure size number: " + d[1]);
							return true;
						}
						catch (final Throwable t)
						{
							EricPackage.logException("Can't generate structure", t);
							s.sendMessage(ChatColor.RED + "Unexpected Exception: " + t.getClass().getSimpleName() + " : " + t.getMessage());
						}
					else
					{
						s.sendMessage(ChatColor.RED + "Unknown structure!");
						final StringJoiner joiner = new StringJoiner(ChatColor.GRAY + ", " + ChatColor.GREEN, "Only permitted: " + ChatColor.GREEN, "");
						for (final StructureType current : StructureType.values())
							joiner.add(current.name().toLowerCase(Locale.ENGLISH));
						s.sendMessage(joiner.toString());
					}
					return true;
				}
				s.sendMessage(ChatColor.RED + "Too few arguments. usage: /create <structure type> [size]");
				return false;
			}
		return false;
	}
}
