package com.eric0210.core.api.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemType
{
	ItemStack sample;
	String name;

	public ItemType(final String _name, final ItemStack i)
	{
		name = _name;
		sample = i;
	}

	public boolean check(final ItemStack i)
	{
		if (i == null)
			return false;
		if (i.getType() == null)
			return false;
		if (!(i.getType() == sample.getType()))
			return false; // check item type
		final ItemMeta im = i.getItemMeta();
		final ItemMeta sim = sample.getItemMeta();
		if (im == null)
			return false;
		if (!im.hasDisplayName())
			return false;
		if (!im.getDisplayName().equalsIgnoreCase(sim.getDisplayName()))
			return false; // check display name

		for (final Enchantment ench : i.getEnchantments().keySet())
		{
			final int enchLevel = i.getEnchantments().get(ench);
			if (!(sample.getEnchantmentLevel(ench) == enchLevel))
				return false;
		}

		return true;
	}

	public String getName()
	{
		return name;
	}

	public ItemStack getItem()
	{
		return sample;
	}
}
