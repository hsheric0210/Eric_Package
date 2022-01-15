package com.eric0210.AdditionalFunctions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.utils.GameUtils;

public class CommandNoDurablity extends CommandBase
{
	private boolean enabled;

	public CommandNoDurablity()
	{
		addCommand("nodur");
		EventManager.onEntityDamage.add(new EventData(this, 0));
		// EventManager.onPlayerItemDamage.add(new EventData(this,1)); for compatiblity
	}

	@Override
	public boolean onCommand(final CommandSender s, final String cmd, final String[] args)
	{
		if (!enabled)
		{
			GameUtils.sendPluginMessage(s, ChatColor.GREEN + "NoDurability mode enabled");
			enabled = true;
			return true;
		}
		s.sendMessage(ChatColor.RED + "NoDurability mode disabled");
		enabled = false;
		return true;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		switch (eventDataIndex)
		{
			case 0:
				final EntityDamageEvent e1 = (EntityDamageEvent) ev;
				final Entity ent = e1.getEntity();
				if (ent instanceof Player)
				{
					if (!enabled)
						return false;
					final Player p = (Player) ent;
					final PlayerInventory inv = p.getInventory();
					if (inv.getHelmet() != null)
						inv.getHelmet().setDurability((short) 0);
					if (inv.getChestplate() != null)
						inv.getChestplate().setDurability((short) 0);
					if (inv.getLeggings() != null)
						inv.getLeggings().setDurability((short) 0);
					if (inv.getBoots() != null)
						inv.getBoots().setDurability((short) 0);
					if (e1 instanceof EntityDamageByEntityEvent)
					{
						final EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e1;
						if (e2.getDamager() instanceof Player)
						{
							final Player damager = (Player) e2.getDamager();
							if (damager.getItemInHand() != null)
								if (hasDurabilityItem(damager.getItemInHand().getType())) {
									final ItemStack item = damager.getItemInHand();
									item.setDurability((short) 0);
									damager.setItemInHand(item);
									damager.updateInventory();
								}
						}
					}
				}
				break;
//		case 1:
//			PlayerItemDamageEvent e2 = (PlayerItemDamageEvent) ev;
//			Player p = e2.getPlayer();
//			if (!enabled)
//				return -1;
//			if (e2.getPlayer().getItemInHand().getDurability() != 0)
//			{
//				e2.getPlayer().getItemInHand().setDurability((short) 0);
//			}
//			for compatiblity
		}

		return false;
	}

	private boolean hasDurabilityItem(final Material type)
	{
		switch (type)
		{
			case DIAMOND_SWORD:
			case GOLD_SWORD:
			case IRON_SWORD:
			case STONE_SWORD:
			case WOOD_SWORD:
			case DIAMOND_PICKAXE:
			case GOLD_PICKAXE:
			case IRON_PICKAXE:
			case STONE_PICKAXE:
			case WOOD_PICKAXE:
			case DIAMOND_AXE:
			case GOLD_AXE:
			case IRON_AXE:
			case STONE_AXE:
			case WOOD_AXE:
			case DIAMOND_SPADE:
			case GOLD_SPADE:
			case IRON_SPADE:
			case STONE_SPADE:
			case WOOD_SPADE:
			case FLINT_AND_STEEL:
				return true;
			default:
				return false;
		}
	}
}
