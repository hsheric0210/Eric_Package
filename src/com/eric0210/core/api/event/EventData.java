package com.eric0210.core.api.event;

import com.eric0210.core.api.EventHandler;

public class EventData
{
	EventHandler base;
	int parameter;
	EventRunnable eventRunner;

	public EventData(final EventHandler base)
	{
		this(base, 0);
	}

	public EventData(final EventHandler base, final int param)
	{
		this.base = base;
		parameter = param;
	}

	public EventData(final EventRunnable eventRunner)
	{
		this.eventRunner = eventRunner;
	}
}
