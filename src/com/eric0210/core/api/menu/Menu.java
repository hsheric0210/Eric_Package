package com.eric0210.core.api.menu;

import java.util.ArrayList;
import java.util.List;

import com.eric0210.core.utils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.eric0210.core.api.EventHandler;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;

import net.minecraft.server.v1_7_R4.EntityPlayer;

public abstract class Menu extends EventHandler
{

	static List<Menu> menus = new ArrayList<>();
	Inventory inv;

	public final void setInventorySetting(final String name, final InventoryType type)
	{
		inv = Bukkit.createInventory(null, type, name);
		EventManager.onInventoryClick.add(new EventData(this));
		menus.add(this);
	}

	public final void setInventorySetting(final String name, final int capacity)
	{
		inv = Bukkit.createInventory(null, capacity, name);
		EventManager.onInventoryClick.add(new EventData(this));
		menus.add(this);
	}

	public final String getName()
	{
		return inv.getName();
	}

	public final Inventory getInventory()
	{
		return inv;
	}

	public void open(final Player p)
	{
		p.closeInventory();
		final EntityPlayer pl = GameUtils.GetHandle.getPlayerHandle(p);
		final CraftInventoryView view = new CraftInventoryView(p, getInventory(), new CraftContainer(getInventory(), p, pl.nextContainerCounter()));
		p.openInventory(view);
	}

	public final void close()
	{
		for (final Player p : GameUtils.getOnlinePlayers())
			if (p.getOpenInventory().getTitle().equals(getInventory().getTitle()))
				p.getOpenInventory().close();
	}

	public final void close(final Player p)
	{
		if (p.getOpenInventory().getTitle().equals(getInventory().getTitle()))
			p.getOpenInventory().close();
	}

	public static final void closeAll()
	{
		for (final Menu m : menus)
			m.close();
	}

	public static final void addMenu(final Menu m)
	{
		menus.add(m);
	}

	public final void setItem(final int slot, final ItemStack i)
	{
		getInventory().setItem(slot, i);
	}

	public final ItemStack getItem(final int slot)
	{
		return getInventory().getItem(slot);
	}
}
