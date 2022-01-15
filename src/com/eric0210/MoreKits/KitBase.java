package com.eric0210.MoreKits;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.eric0210.core.api.EventHandler;

public abstract class KitBase extends EventHandler
{

	private String name;

	public abstract void Init();

	public abstract void give(Player p);

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		return false;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name1)
	{
		name = name1;
	}
}
