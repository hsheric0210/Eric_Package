package com.eric0210.magicswords;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandManager;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemTypeFactory;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.Registerer;
import com.eric0210.magicswords.Items.*;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Magicswords extends Module
{
	public static Magicswords instance;
	public ItemBase bowPack = new BowPack();
	public ItemBase blaze = new Blaze();
	public ItemBase arrowRod = new ArrowRod();
	public ItemBase magicApple = new MagicApple();
	public ItemBase magicStick = new MagicStick();

	@Override
	public void load()
	{
		instance = this;
		try
		{
			final BowPack bp = (BowPack) bowPack;
			CommandManager.registerHandler(bp, "bow");
			bowPack.Init();

			Registerer.RegisterItem(blaze, this);
			Registerer.RegisterItem(arrowRod, this);
			Registerer.RegisterItem(magicApple, this);
			Registerer.RegisterItem(magicStick, this);
		}
		catch (final Throwable e)
		{
			EricPackage.logException("[Magicswords] Can't register magic swords", e);
		}
		EventManager.onEntityDamageByEntity.add(new EventData(this, 0));
		EventManager.onBlockDamage.add(new EventData(this, 1));
	}

	@Override
	public void unload()
	{
		try
		{
			CommandManager.unregisterHandler("bow");
			Registerer.UnregisterItem(blaze);
			Registerer.UnregisterItem(arrowRod);
			Registerer.UnregisterItem(magicApple);
			Registerer.UnregisterItem(magicStick);
		}
		catch (final Throwable t)
		{
			EricPackage.logException("Unregistering items", t);
		}
	}

	@Override
	public boolean onEvent(final Event event, final int eventDataIndex)
	{
		switch (eventDataIndex)
		{
			case 0:
			{
				final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				if (e.getDamager() instanceof Player)
				{
					final Player p = (Player) e.getDamager();
					final String check = ItemTypeFactory.check(p.getItemInHand());
					if (!check.isEmpty())
						e.setDamage(10000.0D);
				}
				return true;
			}
			case 1:
			{
				final BlockDamageEvent e = (BlockDamageEvent) event;
				final String check = ItemTypeFactory.check(e.getPlayer().getItemInHand());
				if (!check.isEmpty())
				{
					final Block block = e.getBlock();
					final Location blockpos = block.getLocation();
					GameUtils.blockBreakEffect(blockpos, block.getType(), 100, 0.3);
					e.getBlock().breakNaturally();
					e.getBlock().getWorld().playSound(blockpos, Sound.ITEM_BREAK, 1.0F, 1.0F);
				}
				return true;
			}
		}
		return false;
	}
}
