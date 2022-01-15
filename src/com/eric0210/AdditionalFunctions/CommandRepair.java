package com.eric0210.AdditionalFunctions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.utils.StringUtils;
import com.eric0210.core.utils.GameUtils;

public class CommandRepair extends CommandBase
{
	public CommandRepair()
	{
		addCommand("repairItem");
	}

	@Override
	public boolean onCommand(final CommandSender s, final String cmd, final String[] args)
	{
		if (GameUtils.isPlayer(s))
		{
			final Player p = (Player) s;
			final ItemStack i = p.getItemInHand();
			if (i != null && i.getType() != Material.AIR)
			{
				i.setDurability((short) 0);
				GameUtils.sendPluginMessage(p, ChatColor.GREEN + "Item was Repaired");
				return true;
			}
			GameUtils.sendPluginMessage(p, ChatColor.RED + "Please held the item for repair.");
			return true;
		}
		StringUtils.Error_MustBePlayer(s);
		return false;
	}

}
