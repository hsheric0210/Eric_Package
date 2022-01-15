package com.eric0210.core.api.item;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class ItemTypeFactory
{
	private static final HashMap<String, ItemType> list = new HashMap<>();

	public static void add(final ItemType it)
	{
		list.put(it.getName(), it);
	}

	public static boolean exist(final String type)
	{
		return list.containsKey(type);
	}

	public static String[] values()
	{
		return (String[]) list.keySet().toArray();
	}

	public static ItemType get(final String type)
	{
		if (list.containsKey(type))
			return list.get(type);
		return null;
	}

	public static String check(final ItemStack is)
	{
		for (final ItemType it : list.values())
			if (it.check(is))
				return it.getName();
		return "";
	}
}
