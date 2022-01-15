package com.eric0210.aooniGame.items;

import java.util.Arrays;

import com.eric0210.aooniGame.AooniGameMainScripter;
import com.eric0210.aooniGame.AooniGameMainScripter.GameStatus;
import com.eric0210.core.EricPackage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class SugarBoost implements SkillBase
{
	BukkitTask cooldownTimerTask;
	ItemStack item;
	long chargeDelay = 5L;
	int cooldownTime = 100;

	public SugarBoost(final int count)
	{
		SkillManager.skills.add(this);

		item = new ItemStack(Material.SUGAR);
		item.setAmount(count);

		final ItemMeta im = item.getItemMeta();
		im.setDisplayName("Sugar Boost");
		im.setLore(Arrays.asList("약 15초 동안 신속 4를 부여합니다.", "우클릭으로 사용할 수 있습니다."));
		im.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
		item.setItemMeta(im);
	}

	@Override
	public ItemStack getItem()
	{
		return item;
	}

	@Override
	public int getSlot()
	{
		return 1;
	}

	@Override
	public void handleEvent(final Event ev)
	{
		final PlayerInteractEvent e = (PlayerInteractEvent) ev;
		if (AooniGameMainScripter.GameScenario == GameStatus.PLAY)
			e.setCancelled(true);
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;

		if (cooldownTime != 100)
		{
			e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + ChatColor.RED + "This ability is on cooldown!");
			e.setCancelled(true);
			return;
		}

		e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + ChatColor.GREEN + "You were used the Sugar Boost!");

		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 305, 6, false), true);

		cooldownTime = 0;
		cooldownTimerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(EricPackage.instance(), () ->
		{
			final ItemMeta im = e.getItem().getItemMeta();
			im.setDisplayName("This ability is on cool down..." + cooldownTime + "% completed");
			e.getItem().setItemMeta(im);
			cooldownTime++;
			e.getPlayer().getInventory().setItem(getSlot(), e.getItem());
			e.getPlayer().updateInventory();
			if (cooldownTime >= 100)
			{
				e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + ChatColor.GREEN + "You can use Sugar Boost again.");
				final ItemMeta im2 = e.getItem().getItemMeta();
				im2.setDisplayName("Sugar Boost");
				e.getItem().setItemMeta(im2);
				cooldownTime = 100;
				e.getPlayer().getInventory().setItem(getSlot(), e.getItem());
				cooldownTimerTask.cancel();
			}
		}, 0L, 10L);

		if (e.getItem().getAmount() > 1)
			e.getItem().setAmount(e.getItem().getAmount() - 1);
		else
		{
			e.getPlayer().getInventory().remove(e.getItem());
			e.getPlayer().updateInventory();
			cooldownTimerTask.cancel();
		}
	}

	@Override
	public boolean isOnlyForAooni()
	{
		return false;
	}
}
