package com.eric0210.MoreKits;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.utils.GameUtils;

public class MoreKitCommand extends CommandBase
{
	public MoreKitCommand()
	{
		addCommand("morekit");
	}

	@Override
	public boolean onCommand(final CommandSender s, final String cmd, final String[] args)
	{
		if (args.length > 0)
		{
			if (GameUtils.isPlayer(s))
			{
				final String param1 = args[0];
				if (KitsList.getTool(param1) != null)
				{
					final KitBase t = KitsList.getTool(param1);
					t.give((Player) s);
					s.sendMessage("Tool " + t.getName() + " is Successful summoned.");
					return true;
				}
				s.sendMessage(String.valueOf(KitsList.ToolList));
				s.sendMessage("Specified Tool " + param1 + " does not exists.");
				return false;
			}
			s.sendMessage("command cannot be handled; invalid sender!");
			return false;
		}
		s.sendMessage("usage: /morekit <toolname>");
		return false;
	}

}
