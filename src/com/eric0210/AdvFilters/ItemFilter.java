package com.eric0210.AdvFilters;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.eric0210.core.EricPackage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemFilter
{
	private final Set<Map.Entry<Material, Short>> filter;

	public ItemFilter()
	{
		filter = AdvFilters.instance().getFilteredItems().parallelStream().filter(s -> Objects.nonNull(s) && !s.isEmpty()).map(s ->
		{
			final String[] split = s.split(":");
			final Material mat = Material.matchMaterial(split[0]);
			if (mat == null)
				return null; // Skip
			if (split.length > 1)
			{

				final short data;
				try
				{
					data = Short.parseShort(split[1]);
				}
				catch (final NumberFormatException e)
				{
					EricPackage.warning("[AdvFilters] [ItemFilter] Malformed data(a.k.a. durability, damage) number of blacklisted item in filtered item list file line " + s);
					return null; // Skip
				}
				return (Map.Entry<Material, Short>) new AbstractMap.SimpleImmutableEntry(mat, data);
			}
			else
				return (Map.Entry<Material, Short>) new AbstractMap.SimpleImmutableEntry(mat, -1);
		}).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	public boolean isBlocked(final ItemStack item)
	{
		return filter.parallelStream().anyMatch(entry ->
		{
			final Material itemMat = item.getType();
			final short data = item.getDurability();

			if (entry.getValue() == -1)
				return entry.getKey() == itemMat;
			return entry.getKey() == itemMat && entry.getValue() == data;
		});
	}
}
