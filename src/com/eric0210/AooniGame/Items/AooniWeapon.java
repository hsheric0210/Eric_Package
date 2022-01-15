package com.eric0210.aooniGame.items;

import java.util.Arrays;

import com.eric0210.aooniGame.AooniGameMainScripter;
import com.eric0210.core.EricPackage;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

public class AooniWeapon implements SkillBase
{
	private static final String SKILL_NAME = "도약";
	BukkitTask cooldownTimerTask;
	ItemStack item;
	long chargeDelay = 2;

	public AooniWeapon(final int count)
	{
		SkillManager.skills.add(this);

		item = new ItemStack(Material.STONE_SWORD);
		item.setAmount(count);

		final ItemMeta im = item.getItemMeta();
		im.setDisplayName("아오오니 무기");
		im.setLore(Arrays.asList("아오오니가 아닌 사람들을 한 방에 죽일 수 있습니다.", "막기 시 앞으로 빠른 속도로 도약할 수 있습니다.", "아오오니 전용."));
		im.addEnchant(Enchantment.DURABILITY, 10, true);
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
	public void handleEvent(final Event event)
	{
		final PlayerInteractEvent e = (PlayerInteractEvent) event;

		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;

		if (e.getItem().getDurability() != 0)
		{
			e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + String.format(SkillBase.SKILL_IN_COOLDOWN, SKILL_NAME));
			return;
		}

		// <editor-fold desc="Skill affect">
		GameUtils.sphereEffect(e.getPlayer().getLocation(), Effect.EXPLOSION_LARGE, null, 5, 1);
		e.getPlayer().setVelocity(e.getPlayer().getVelocity().add(e.getPlayer().getLocation().getDirection().multiply(1.5F)));
		// </editor-fold>

		// <editor-fold desc="Cooldown">
		e.getItem().setDurability((short) (ItemUtils.getItemDefaultDurability(2) - 1));
		cooldownTimerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(EricPackage.instance(), () ->
		{
			final short newCoolDown = (short) (e.getItem().getDurability() - 5);

			e.getPlayer().updateInventory();
			if (newCoolDown <= 0)
			{
				e.getItem().setDurability((short) 0);
				cooldownTimerTask.cancel();
				e.getPlayer().sendMessage(AooniGameMainScripter.prefix() + String.format(SkillBase.SKILL_COOLDOWN_FINISHED, SKILL_NAME));
			}
			else
				e.getItem().setDurability(newCoolDown);
		}, 0L, chargeDelay * 10);
		// </editor-fold>
	}

	@Override
	public boolean isOnlyForAooni()
	{
		return true;
	}
}
