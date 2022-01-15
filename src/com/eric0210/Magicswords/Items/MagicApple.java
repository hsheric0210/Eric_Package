package com.eric0210.magicswords.Items;

import java.util.HashSet;

import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemType;
import com.eric0210.core.api.item.ItemTypeFactory;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;
import com.eric0210.magicswords.Magicswords;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MagicApple extends ItemBase
{
	private ItemStack ma;

	@Override
	public void Init()
	{
		setItemName("Magic-Apple");
		setItemCommand("ma");
		ma = ItemUtils.createItem(Magicswords.instance.getModuleConfig().get("magicapple.Name", "마사과"), Material.APPLE, 1, true, 975, 9999, null);
		EventManager.onPlayerItemConsume.add(new EventData(this, 0));
		ItemTypeFactory.add(new ItemType("MagicApple", ma));
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String cmd, final String[] args)
	{
		if (sender.isOp() && cmd.equalsIgnoreCase(getItemCommand()) && sender instanceof Player)
			if (GameUtils.isPlayer(sender))
			{
				final Player p = (Player) sender;
				p.getInventory().addItem(ma);
				return true;
			}
		return false;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		final PlayerItemConsumeEvent e = (PlayerItemConsumeEvent) ev;
		if (e.getPlayer() != null && e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals(ma.getItemMeta().getDisplayName()))
		{
			final Player p = e.getPlayer();
			final HashSet<PotionEffect> pots = new HashSet<>();
			pots.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255, false));
			pots.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, 255, false));
			pots.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, 15, false));
			pots.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 10, false));
			pots.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 1, false));
			pots.add(new PotionEffect(PotionEffectType.SPEED, 60, 5, false));
			pots.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 60, 1, false));
			pots.add(new PotionEffect(PotionEffectType.JUMP, 60, 5, false));
			pots.add(new PotionEffect(PotionEffectType.REGENERATION, 60, 255, false));
			pots.add(new PotionEffect(PotionEffectType.SATURATION, 60, 255, false));
			p.addPotionEffects(pots);
			return true;
		}
		return false;
	}
}
