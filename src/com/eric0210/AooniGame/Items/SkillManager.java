package com.eric0210.aooniGame.items;

import java.util.ArrayList;

import com.eric0210.aooniGame.AooniGameMainScripter;
import com.eric0210.aooniGame.AooniGameMainScripter.GameStatus;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkillManager
{
	static ArrayList<SkillBase> skills = new ArrayList<>();
	public static SkillBase kbsword = new KnockbackSword(3);
	public static SkillBase locPrinter = new LocationPrinter(2);
	public static SkillBase aooniWep = new AooniWeapon(1);
	public static SkillBase sugarboost = new SugarBoost(3);

	AooniGameMainScripter scripter;

	public SkillManager(final AooniGameMainScripter scripter)
	{
		this.scripter = scripter;
	}

	public static boolean isGameItem(final ItemStack i)
	{
		if (i == null || i.getType() == Material.AIR)
			return false;
		for (final SkillBase skill : skills)
		{
			final ItemStack skillitem = skill.getItem();
			if (skillitem.hasItemMeta() && i.hasItemMeta())
				if (skillitem.getItemMeta().hasDisplayName() && i.getItemMeta().hasDisplayName())
					if (skillitem.getItemMeta().getDisplayName().equalsIgnoreCase(i.getItemMeta().getDisplayName()))
						return true;
		}
		return false;
	}

	public void onGameStartHook()
	{
	}

	public void onAooniChoosedHook(final Player aooni)
	{
		for (final SkillBase skill : skills)
			if (skill.isOnlyForAooni())
				aooni.getInventory().setItem(skill.getSlot(), skill.getItem());
			else
				for (final Player p : GameUtils.getOnlinePlayers())
				{
					if (p.equals(aooni))
						continue; // If you want solo play for item debugging, remove this code.
					p.getInventory().setItem(skill.getSlot(), skill.getItem());
				}
		aooni.getInventory().setBoots(ItemUtils.createItem("", Material.IRON_BOOTS, 1, false, 1, 6754, null));
		aooni.getInventory().setChestplate(ItemUtils.createItem("", Material.IRON_CHESTPLATE, 1, false, 1, 5634, null));
	}

	public void InteractHook(final PlayerInteractEvent e)
	{
		if (!(AooniGameMainScripter.GameScenario == AooniGameMainScripter.GameStatus.PLAY))
			return;
		if (e.getItem() == null)
			return;
		if (e.getItem().hasItemMeta())
		{
			final ItemMeta mt = e.getItem().getItemMeta();
			if (mt.hasDisplayName())
				for (final SkillBase skill : skills)
				{
					if (skill.getItem().getType() == Material.WOOD_SWORD)
						continue;
					if (mt.getDisplayName().equals(skill.getItem().getItemMeta().getDisplayName()))
						skill.handleEvent(e);
				}
		}
	}

	public void EntityDamageByEntityEventHook(final EntityDamageByEntityEvent e)
	{
		if (!(AooniGameMainScripter.GameScenario == AooniGameMainScripter.GameStatus.PLAY) || !GameUtils.isPlayer(e.getDamager()))
			return;
		final Player p = (Player) e.getDamager();
		if (p.getItemInHand() == null)
			return;
		if (p.getItemInHand().hasItemMeta())
		{
			final ItemMeta mt = p.getItemInHand().getItemMeta();
			if (mt.getDisplayName().equals(kbsword.getItem().getItemMeta().getDisplayName()))
				kbsword.handleEvent(e);
		}
	}

	public void InventoryHook(final InventoryClickEvent e)
	{
		if (!(AooniGameMainScripter.GameScenario == AooniGameMainScripter.GameStatus.PLAY))
			return;
		if (AooniGameMainScripter.GameScenario == GameStatus.NOPLAY)
			return;
		for (final SkillBase skill : skills)
			if (e.getCurrentItem().hasItemMeta())
				if (e.getCurrentItem().getItemMeta().hasDisplayName())
					if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(skill.getItem().getItemMeta().getDisplayName()))
						e.setCancelled(true);
	}
}
