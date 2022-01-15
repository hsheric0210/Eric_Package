package com.eric0210.AdditionalFunctions;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.api.command.CommandRegisterationException;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.utils.Registerer;

import org.bukkit.event.Event;

public class AdditionalFunctions extends Module
{
	private final CommandBase cmdMaxStack = new CommandMaxStack();
	private final CommandBase cmdNoDurability = new CommandNoDurablity();
	private final CommandBase cmdRepair = new CommandRepair();
	static AdditionalFunctions instance;

	@Override
	public void load()
	{
		instance = this;
		try
		{
			Registerer.RegisterCommand(cmdMaxStack);
			Registerer.RegisterCommand(cmdNoDurability);
			Registerer.RegisterCommand(cmdRepair);

			EventManager.onPlayerJoin.add(new EventData(this, 0));
			EventManager.onPlayerQuit.add(new EventData(this, 1));
			EventManager.onPlayerKick.add(new EventData(this, 2));
			EventManager.onAsyncPlayerChat.add(new EventData(this, 3));
			EventManager.onPlayerDeath.add(new EventData(this, 4));
			EventManager.onPlayerInteract.add(new EventData(this, 5));
			EventManager.onEntityDamageByEntity.add(new EventData(this, 6));
		}
		catch (final CommandRegisterationException e)
		{
			EricPackage.logException("Failed to register commands for AdditionalFunctions", e);
		}
	}

	@Override
	public void unload()
	{
		Registerer.UnregisterCommand(cmdMaxStack);
		Registerer.UnregisterCommand(cmdNoDurability);
		Registerer.UnregisterCommand(cmdRepair);
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		return new AdditionalMessages().onEvent(ev, eventDataIndex) || new AdditionalDeathEffect().onEvent(ev, eventDataIndex);
	}
}
