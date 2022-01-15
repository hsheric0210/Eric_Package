package com.eric0210.core.api.item;

import java.util.ArrayList;
import java.util.List;

import com.eric0210.core.api.EventHandler;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.ICommandHandler;

import org.bukkit.event.Event;

public abstract class ItemBase extends EventHandler implements ICommandHandler
{
	public static List<ItemBase> items = new ArrayList<>();
	private String itemName;
	private String itemCommand;
	private Module module;

	@Override
	public final boolean checkHandlerAndProcessEvent(final Event ev, final int CustomData)
	{
		if (module == null || module.isModuleEnabled())
			return onEvent(ev, CustomData);
		return false;
	}

	public abstract void Init();

	// <editor-fold desc="Getter/Setters">
	public final void setItemCommand(final String command)
	{
		itemCommand = command;
	}

	public final void setModule(final Module module)
	{
		this.module = module;
	}

	public final void setItemName(final String itemName)
	{
		this.itemName = itemName;
	}

	public final String getItemName()
	{
		return itemName;
	}

	public final String getItemCommand()
	{
		return itemCommand;
	}
	// </editor-fold>

	public enum UsageType
	{
		Magicsword,
		Utility,
		Enchantment
	}
}
