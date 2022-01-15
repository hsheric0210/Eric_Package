package com.eric0210.core.utils;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.api.command.CommandManager;
import com.eric0210.core.api.command.CommandRegisterationException;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemRegisterationException;

public class Registerer
{
	public static final void RegisterItem(final ItemBase item, final Module module) throws ItemRegisterationException
	{
		try
		{
			item.setModule(module);
			item.Init();
			CommandManager.registerHandler(item, item.getItemCommand());
		}
		catch (final NullPointerException e)
		{
			throw new ItemRegisterationException("Item module register failed :" + e.getMessage(), e);
		}
	}

	public static final void RegisterCommand(final CommandBase c) throws CommandRegisterationException
	{
		try
		{
			CommandManager.registerHandler(c, c.getCommands().toArray(new String[0]));
		}
		catch (final NullPointerException e)
		{
			throw new CommandRegisterationException("Command module register failed :" + e.getMessage(), e);
		}
	}

	public static final void UnregisterItem(final ItemBase b)
	{
		CommandManager.unregisterHandler(b.getItemCommand());
	}

	public static final void UnregisterCommand(final CommandBase c)
	{
		CommandManager.unregisterHandler(c.getCommands().toArray(new String[0]));
	}

	public static final Object getConfigValue(final String moduleName, final String k, final Object defaultvalue)
	{
		try
		{
			final Module m = Module.getByName(moduleName);
			if (m != null)
			{
				final Object conf = m.getModuleConfig().get(k, defaultvalue);
				return conf;

			}
			throw new IllegalArgumentException("Unable to load config data: module " + moduleName + " not found");
		}
		catch (final Throwable t)
		{
			EricPackage.logException("Can't load config value in " + moduleName + ".cfg", t);
		}
		return null;
	}
}
