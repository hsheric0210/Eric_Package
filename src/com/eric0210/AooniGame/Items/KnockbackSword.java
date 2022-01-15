package com.eric0210.aooniGame.items;

import java.util.Arrays;
import java.util.Collections;

import com.eric0210.aooniGame.AooniGameMainScripter;
import com.eric0210.core.EricPackage;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

public class KnockbackSword implements SkillBase
{
	BukkitTask cooldownTimerTask;
	ItemStack item;
	long chargeDelay = 3;

	public KnockbackSword(final int count)
	{
		SkillManager.skills.add(this);

		item = new ItemStack(Material.WOOD_SWORD);
		item.setAmount(count);

		final ItemMeta im = item.getItemMeta();
		im.setDisplayName("밀치기 막대기");
		im.setLore(Collections.singletonList("아오오니를 밀치기 10의 힘으로 멀리 날려버립니다."));
		im.addEnchant(Enchantment.KNOCKBACK, 10, true);
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
		return 0;
	}

	@Override
	public void handleEvent(final Event ev)
	{
		final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
		if (!GameUtils.isPlayer(e.getDamager()))
			return;
		final Player damager = (Player) e.getDamager();
		final ItemStack item = damager.getItemInHand();
		if (item.getDurability() != 0)
		{
			damager.sendMessage(AooniGameMainScripter.prefix() + ChatColor.RED + "��ٿ��� �Ϸ���� �ʾҽ��ϴ�!");
			e.setCancelled(true);
			return;
		}
		e.setCancelled(false);

		damager.sendMessage(AooniGameMainScripter.prefix() + ChatColor.YELLOW + "��ġ�� ����⸦ ����Ͽ����ϴ�.");

		item.setDurability((short) (ItemUtils.getItemDefaultDurability(1) - 1));
		cooldownTimerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(EricPackage.instance(), () ->
		{
			final short newCoolDown = (short) (item.getDurability() - 2);
			final ItemMeta im = item.getItemMeta();
			im.removeEnchant(Enchantment.KNOCKBACK);
			item.setItemMeta(im);

			damager.updateInventory();
			if (newCoolDown <= 0)
			{
				damager.sendMessage(AooniGameMainScripter.prefix() + ChatColor.GREEN + "��ġ�� ����⸦ �ٽ� ����� �� �ֽ��ϴ�.");
				final ItemMeta im2 = item.getItemMeta();
				im2.addEnchant(Enchantment.KNOCKBACK, 10, true);
				item.setItemMeta(im2);
				item.setDurability((short) 0);
				cooldownTimerTask.cancel();
			}
			else
				item.setDurability(newCoolDown);
		}, 1L, chargeDelay * 10);

		if (item.getAmount() > 1)
			item.setAmount(item.getAmount() - 1);
		else
		{
			damager.getInventory().remove(item);
			damager.updateInventory();
		}
	}

	@Override
	public boolean isOnlyForAooni()
	{
		return false;
	}
}
