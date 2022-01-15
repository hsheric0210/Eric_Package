package com.eric0210.AdvFilters;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ChatFilterEvent extends PlayerEvent implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private final String originalChat;
	private String filteredChat;

	public ChatFilterEvent(final Player who, final String originalChat, final String filteredChat)
	{
		super(who);
		this.originalChat = originalChat;
		this.filteredChat = filteredChat;
	}

	public String getOriginalMessage()
	{
		return filteredChat;
	}

	public String getFilteredMessage()
	{
		return filteredChat;
	}

	public void setFilteredMessage(final String newChatMessage)
	{
		filteredChat = newChatMessage;
	}

	public boolean isCancelled()
	{
		return cancel;
	}

	public void setCancelled(final boolean cancel)
	{
		this.cancel = cancel;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

}
