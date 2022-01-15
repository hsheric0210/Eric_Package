package com.eric0210.core.api;

import org.bukkit.event.Event;

@FunctionalInterface
public interface IEventHandler
{
	boolean onEvent(Event ev, int eventDataIndex);
}
