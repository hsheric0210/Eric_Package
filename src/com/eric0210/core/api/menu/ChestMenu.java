package com.eric0210.core.api.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.eric0210.core.EricPackage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestMenu extends Menu
{
	private final HashMap<String, Run> events = new HashMap<>();

	public ChestMenu(final String inventoryname)
	{
		setInventorySetting(inventoryname, InventoryType.CHEST);
		addMenu(this);
	}

	public ChestMenu(final String inventoryname, final int capacity)
	{
		setInventorySetting(inventoryname, capacity);
		addMenu(this);
	}

	public final void setButton(final ItemStack item, final String name, final int slot, final List<String> lore, final Run onClicked)
	{
		final ItemStack i = new ItemStack(item);
		final ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(name);
		if (lore != null)
		{
			final ArrayList<String> lore2 = new ArrayList<>();
			for (final String str : lore)
				lore2.add(ChatColor.RESET + str);
			meta.setLore(lore2);
		}
		i.setItemMeta(meta);
		setItem(slot, i);
		events.put(ChatColor.stripColor(name), onClicked);
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		final InventoryClickEvent e1 = (InventoryClickEvent) ev;
		if (e1.getInventory() != null && e1.getCurrentItem() != null && e1.getWhoClicked() != null)
			if (e1.getInventory().getName().equals(getInventory().getName())) {
				e1.setCancelled(true);
				if (e1.getCurrentItem().getItemMeta() == null)
					return false;
				final String name = ChatColor.stripColor(e1.getCurrentItem().getItemMeta().getDisplayName());
				final Run r = events.get(name);
				if (r != null) {
					new BukkitRunnable() {

						@Override
						public void run() {
							r.run(e1.getInventory(), (Player) e1.getWhoClicked());
						}
					}.runTask(EricPackage.instance());

					return true;
				}
			}
		return false;
	}

	public interface Run
	{
		void run(Inventory inv, Player p);
	}
}
