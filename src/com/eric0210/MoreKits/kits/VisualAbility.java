package com.eric0210.MoreKits.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.eric0210.MoreKits.KitBase;
import com.eric0210.MoreKits.KitsList;

public class VisualAbility extends KitBase
{
	private ItemStack Sword;
	private ItemStack Iron;
	private ItemStack Gold;
	private ItemStack Bookshelf;
	private ItemStack WaterBukkit;

	public VisualAbility()
	{
		KitsList.ToolList.add(this);
	}

	@Override
	public void Init()
	{
		setName("visualability");
		Sword = new ItemStack(Material.WOOD_SWORD, 1);
		Iron = new ItemStack(Material.IRON_INGOT, 64);
		Gold = new ItemStack(Material.GOLD_INGOT, 64);
		Bookshelf = new ItemStack(Material.BOOKSHELF, 64);
		WaterBukkit = new ItemStack(Material.WATER_BUCKET, 1);
	}

	@Override
	public void give(final Player p)
	{
		final Inventory inv = p.getInventory();
		inv.setItem(0, Sword);
		inv.setItem(1, Iron);
		inv.setItem(2, Gold);
		inv.setItem(9, Bookshelf);
		inv.setItem(8, Bookshelf);
		inv.setItem(3, WaterBukkit);
	}
}
