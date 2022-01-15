package com.eric0210.core.api.command;

import org.bukkit.command.CommandSender;

public interface ICommandHandler
{
	boolean onCommand(CommandSender s, String cmd, String[] args);
}
