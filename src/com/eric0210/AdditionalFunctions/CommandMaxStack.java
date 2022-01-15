package com.eric0210.AdditionalFunctions;

import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMaxStack extends CommandBase
{
	public CommandMaxStack()
	{
		addCommand("maxstack");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String cmd, final String[] args)
	{
		if (GameUtils.isPlayer(sender))
		{
			final Player p = (Player) sender;
			final ItemStack i = p.getItemInHand();
			if (i != null && i.getType() != Material.AIR)
			{
				final int maxStackSize = i.getMaxStackSize();
				if (i.getAmount() < maxStackSize)
				{
					i.setAmount(maxStackSize);
					GameUtils.sendPluginMessage(p, ChatColor.GREEN + "Item is filled with max stack size");
				}
				else
					GameUtils.sendPluginMessage(p, ChatColor.YELLOW + "Item was already filled with max stack size");
				return true;
			}
			GameUtils.sendPluginMessage(p, ChatColor.RED + "Please hold the item for tamper stack size.");
			return true;
		}
		StringUtils.Error_MustBePlayer(sender);
		return false;
	}
}
