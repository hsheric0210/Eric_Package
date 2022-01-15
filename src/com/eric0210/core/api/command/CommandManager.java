package com.eric0210.core.api.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import com.eric0210.core.EricPackage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandManager
{
	private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
	public static HashMap<String, ICommandHandler> commandsMap = new HashMap<>();

	public static boolean dispatch(final CommandSender sender, final String fullCommandline)
	{
		String fixedCommandline = fullCommandline;

		// Lower case
		fixedCommandline = fixedCommandline.toLowerCase(Locale.ENGLISH);

		// Remove '/' character (command identifier on chat)
		fixedCommandline = fixedCommandline.replaceFirst("/", "");

		// Translate color char
		fixedCommandline = ChatColor.translateAlternateColorCodes('&', fixedCommandline);

		// Split commandline by space character
		final String[] splatted = PATTERN_ON_SPACE.split(fixedCommandline);

		final String command = splatted[0];

		final String[] arguments = new String[splatted.length - 1];
		if (splatted.length > 1)
			for (int i = 0, j = splatted.length - 1; i < j; i++)
				arguments[i] = splatted[i + 1];

		EricPackage.debug(sender.getName() + " issued command " + command + " with argument " + Arrays.toString(arguments));

		boolean handleResult = false;

		if (commandsMap.containsKey(command))
			try
			{
				final ICommandHandler handler = commandsMap.get(command);
				EricPackage.debug(command + " handled with handler: " + handler.getClass().getCanonicalName());
				handleResult = handler.onCommand(sender, command, arguments);
			}
			catch (final Throwable t)
			{
				sender.sendMessage(EricPackage.PLUGIN_MESSAGE_FORMAT + ChatColor.RED + "Can't handle command because of an exception : " + t.toString());
				EricPackage.logException("Failed to handle command " + command, t);
			}

		return handleResult;
	}

	public static boolean containsCommand(final String fullCommandline)
	{
		String fixedCommandline = fullCommandline;
		fixedCommandline = fixedCommandline.toLowerCase(Locale.ENGLISH);
		fixedCommandline = fixedCommandline.replaceFirst("/", "");
		fixedCommandline = ChatColor.translateAlternateColorCodes('&', fixedCommandline);
		final String[] splitCommand = PATTERN_ON_SPACE.split(fixedCommandline);
		final String command = splitCommand[0];
		return commandsMap.containsKey(command);
	}

	public static void registerHandler(final ICommandHandler handler, final String... commands)
	{
		for (final String command : commands)
			commandsMap.put(command, handler);
	}

	public static void unregisterHandler(final String... commands)
	{
		for (final String command : commands)
			commandsMap.remove(command);
	}
}
