package com.eric0210.aooniGame.items;

import java.util.Arrays;

import com.eric0210.aooniGame.AooniGameMainScripter;
import com.eric0210.aooniGame.AooniGameMainScripter.GameStatus;
import com.eric0210.core.EricPackage;
import com.eric0210.core.utils.GameUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

/*
 * ���� e.getItem().setDurability() ȥ�ڸ� ����Ͽ��� �� ���������� ���� �ʴ´ٸ�
 * e.getPlayer().getInventory().setItem(getSlot(),e.getItem());�� �Բ� ����ϸ� �� �۵��Ǵ� ���� Ȯ���Ͽ����ϴ�.
 */

public class LocationPrinter implements SkillBase
{
	BukkitTask cooldownTimerTask;
	ItemStack item;
	long chargeDelay = 20L;
	int cooldownTime;

	public LocationPrinter(final int count)
	{
		SkillManager.skills.add(this);

		item = new ItemStack(Material.FISHING_ROD);
		item.setAmount(count);

		final ItemMeta im = item.getItemMeta();
		im.setDisplayName("위치 표시기");
		im.setLore(Arrays.asList("플레이어들의 현재 X,Z 좌표를 공개합니다.", "우클릭으로 사용할 수 있습니다.", "아오오니 전용."));
		im.addEnchant(Enchantment.DURABILITY, 5, true);
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
		if (e.getItem().getDurability() != 0)
		{
			e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + ChatColor.RED + "��ٿ��� �Ϸ���� �ʾҽ��ϴ�!");
			e.setCancelled(true);
			return;
		}

		e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + ChatColor.YELLOW + "��ġ ǥ�ñ⸦ ����Ͽ����ϴ�.");

		for (final Player p : GameUtils.getOnlinePlayers())
			e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + String.format("%s %s - X: %f  Z: %f", p.getName().equals(AooniGameMainScripter.aooniPlayerName) ? ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "[ME]" : ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString(), p.getName(), p.getLocation().getX(), p.getLocation().getZ()));

		e.getItem().setDurability((short) 63);
		cooldownTimerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(EricPackage.instance(), () ->
		{
			e.getItem().setDurability((short) (e.getItem().getDurability() - 10));
			e.getPlayer().getInventory().setItem(getSlot(), e.getItem());
			e.getPlayer().updateInventory();
			if (e.getItem().getDurability() <= 0)
			{
				e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + ChatColor.GREEN + "��ġ ǥ�ñ⸦ �ٽ� ����� �� �ֽ��ϴ�.");
				e.getItem().setDurability((short) 0);
				e.getPlayer().getInventory().setItem(getSlot(), e.getItem());
				cooldownTimerTask.cancel();
			}
		}, 0L, chargeDelay * 10);

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
		return true;
	}
}
