package com.eric0210.core.api;

import org.bukkit.event.Event;

public abstract class EventHandler implements IEventHandler
{

	public boolean checkHandlerAndProcessEvent(final Event ev, final int CustomData)
	{
		return onEvent(ev, CustomData);
	}

	public boolean ExecuteEvent(final Event event, final int eventDataIndex)
	{
		return checkHandlerAndProcessEvent(event, eventDataIndex);
	}
}
