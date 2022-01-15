package com.eric0210.core.api.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.eric0210.core.api.EventHandler;

import org.bukkit.event.Event;

public abstract class CommandBase extends EventHandler implements ICommandHandler
{
	private final Set<String> command = new HashSet<>();

	public final void addCommand(final String s)
	{
		command.add(s);
	}

	public final Collection<String> getCommands()
	{
		return command;
	}

	public final void removeCommand(final String s)
	{
		command.remove(s);
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		return false;
	}
}
