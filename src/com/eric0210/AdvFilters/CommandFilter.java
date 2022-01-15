package com.eric0210.AdvFilters;

import java.util.Locale;
import java.util.regex.Pattern;

import com.eric0210.core.utils.GameUtils;

import org.bukkit.command.CommandSender;

public class CommandFilter
{
	private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

	public CommandFilter()
	{
	}

	public static boolean checkCommand(final String originalCommandline, final CommandSender sender)
	{
		if (sender != null && originalCommandline != null && !originalCommandline.isEmpty())
		{
			String fixedCommandline = originalCommandline.replaceFirst("/", "");
			fixedCommandline = fixedCommandline.toLowerCase(Locale.ENGLISH);

			final String[] splitCommand = PATTERN_ON_SPACE.split(fixedCommandline);
			if (splitCommand.length >= 1)
			{
				final String command = splitCommand[0];

				if (!command.isEmpty())
				{
					final boolean byPlayer = canUsedByPlayer(command);
					final boolean byConsole = canUsedInConsole(command);

					if (!byPlayer && GameUtils.isPlayer(sender))
						return false;
					else
						return byConsole;
				}
			}
			else
				return false;
		}
		return false;
	}

	private static boolean canUsedByPlayer(final String command)
	{
		final Object result = AdvFilters.instance().getModuleConfig().getNoCreate("BlockedCommands." + command + ".player");
		return result == null || (boolean) result;
	}

	private static boolean canUsedInConsole(final String command)
	{
		final Object result = AdvFilters.instance().getModuleConfig().getNoCreate("BlockedCommands." + command + ".console");
		return result == null || (boolean) result;
	}
}
