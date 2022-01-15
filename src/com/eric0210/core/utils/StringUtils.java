package com.eric0210.core.utils;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StringUtils
{
	public static final void Error_MustBePlayer(final CommandSender p)
	{
		p.sendMessage(ChatColor.RED + "이 명령어는 플레이어만이 사용할 수 있습니다. 게임에 접속하여 사용해주세요.");
	}

	public static final void Error_InsufficientPermission(final CommandSender p)
	{
		p.sendMessage(ChatColor.RED + "권한이 부족합니다");
	}

	public static final String getPrefix(final ChatColor prefixColor, final String prefix)
	{
		ChatColor fixedPrefixColor = prefixColor;
		if (fixedPrefixColor == null)
			fixedPrefixColor = ChatColor.GRAY;
		return fixedPrefixColor + ChatColor.BOLD.toString() + prefix + ChatColor.RESET.toString() + ChatColor.GRAY + "> " + ChatColor.RESET;
	}

	public static final String getBracketPrefix(final ChatColor prefixColor, final String prefix)
	{
		ChatColor fixedPrefixColor = prefixColor;
		if (fixedPrefixColor == null)
			fixedPrefixColor = ChatColor.GRAY;
		return ChatColor.GRAY + "[" + fixedPrefixColor + prefix + ChatColor.GRAY + "] " + ChatColor.RESET;
	}

	public static final ChatColor randomChatColor()
	{
		final Random random = new Random();
		final ChatColor[] colors =
		{
				ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE
		};

		return colors[random.nextInt(colors.length)];
	}
}
