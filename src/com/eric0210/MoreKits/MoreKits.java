package com.eric0210.MoreKits;

import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandManager;
import com.eric0210.core.utils.Registerer;

import org.bukkit.event.Event;

public class MoreKits extends Module
{
	MoreKitCommand command;

	@Override
	public void load()
	{
		KitsList.InitTools();
		command = new MoreKitCommand();
		CommandManager.registerHandler(command, "morekit");
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		return false;
	}

	@Override
	public void unload()
	{
		Registerer.UnregisterCommand(command);
	}
}
