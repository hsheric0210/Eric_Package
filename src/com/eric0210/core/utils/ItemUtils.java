package com.eric0210.core.utils;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils
{

	public static final ItemStack getPlayerSkull(final String skullownername)
	{
		final ItemStack i = new ItemStack(Material.SKULL_ITEM, 1);
		i.setDurability((short) SkullType.PLAYER.ordinal());
		final SkullMeta meta = (SkullMeta) i.getItemMeta();
		meta.setOwner(skullownername);
		i.setItemMeta(meta);
		return i;
	}

	public static final ItemStack createItem(final String name, final Material type, final int Amount, final boolean allenchant, final int allenchant_level, final int inspectionNum, final List<String> lore)
	{
		final ItemStack it = new ItemStack(type, Amount);
		final ItemMeta itm = it.getItemMeta();
		if (allenchant)
			for (final Enchantment ench : Enchantment.values()) {
				if (ench == Enchantment.DURABILITY)
					continue;
				itm.addEnchant(ench, allenchant_level, true);
			}

		if (lore != null)
			itm.setLore(lore);

		itm.addEnchant(Enchantment.DURABILITY, inspectionNum, true);
		itm.setDisplayName(name);
		it.setItemMeta(itm);
		return it;
	}

	public static final ItemStack createItemLores(final String itemname, final Material itemtype, final int amount, final List<String> lores)
	{
		final ItemStack i = new ItemStack(itemtype, amount);
		final ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(itemname);
		meta.setLore(lores);
		i.setItemMeta(meta);
		return i;
	}

	public static final ItemStack createWithData(final Material mat, final int num, final short data)
	{
		final ItemStack item = new ItemStack(mat, num);
		item.setDurability(data);
		return item;
	}

	public static final ItemStack createColoredBlock(final Material mat, final int num, final DyeColor color)
	{
		final ItemStack item = new ItemStack(mat, num);
		item.setDurability(color.getWoolData());
		return item;
	}

	public static final short getItemDefaultDurability(final int onewood_twostone_threeiron_fourgold_fivediamond)
	{
		switch (onewood_twostone_threeiron_fourgold_fivediamond)
		{
			case 1:
				// Wooden sword
				return 59;
			case 2:
				// Stone sword
				return 131;
			case 3:
				// Iron sword
				return 250;
			case 4:
				// Golden sword
				return 32;
			case 5:
				// Diamond sword
				return 1561;
			default:
				return 0;
		}
	}

	public static final ItemStack recreateItemWithDurability(final ItemStack original, final short durability)
	{
		final ItemStack newitem = new ItemStack(original.getType(), original.getAmount());
		newitem.setDurability(durability);
		final ItemMeta newim = newitem.getItemMeta();
		final ItemMeta originalim = original.getItemMeta();
		newim.setLore(originalim.getLore());
		newim.setDisplayName(originalim.getDisplayName());
		for (final Entry<Enchantment, Integer> ench : originalim.getEnchants().entrySet())
			newim.addEnchant(ench.getKey(), ench.getValue(), true);
		newitem.setItemMeta(newim);
		return newitem;
	}

	public static final void replaceIteminInventory(final Player p, final ItemStack original, final ItemStack newitem)
	{
		final PlayerInventory inv = p.getInventory();
		final int slot = inv.first(original);
		inv.remove(original);
		inv.setItem(slot, newitem);
		p.updateInventory();
	}
}
