package com.eric0210.aooniGame;

import com.eric0210.aooniGame.AooniGameMainScripter.GameStatus;
import com.eric0210.aooniGame.items.SkillManager;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class AooniGame extends Module
{
	public volatile boolean eventrun = true;
	public AooniGameMainScripter mainScripter;

	public AooniGame()
	{
		EventManager.onEntityDamageByEntity.add(new EventData(this, 0));
		EventManager.onPlayerMove.add(new EventData(this, 1));
		EventManager.onEntityDamage.add(new EventData(this, 2));
		EventManager.onPlayerDeath.add(new EventData(this, 3));
		EventManager.onPlayerDropItem.add(new EventData(this, 4));
		EventManager.onPlayerInteract.add(new EventData(this, 5));
		EventManager.onInventoryClick.add(new EventData(this, 6));
		EventManager.onEntityDamageByEntity.add(new EventData(this, 7));
	}

	@Override
	public void load()
	{
		setModuleCommands("aooni");
		mainScripter = new AooniGameMainScripter(this);
		mainScripter.aooniCustomName = getModuleConfig().get("aooniCustomName", "Aooni");
		mainScripter.AooniCustomNameVisible = getModuleConfig().get("aooniCustomNameVisible", true);
		AooniGameMainScripter.GameScenario = GameStatus.NOPLAY;
		eventrun = true;
	}

	@Override
	public void unload()
	{
		eventrun = false;
		if (AooniGameMainScripter.GameScenario != GameStatus.NOPLAY)
		{
			AooniGameMainScripter.GameScenario = GameStatus.NOPLAY;
			AooniGameMainScripter.aooniTimer.aooni.setCustomName(mainScripter.aooniCustomName);
			AooniGameMainScripter.aooniTimer.aooni.setCustomNameVisible(mainScripter.AooniCustomNameVisible);
			AooniGameMainScripter.aooniTimer.stop();
		}
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		switch (eventDataIndex)
		{
			case 0:
				final EntityDamageByEntityEvent e1 = (EntityDamageByEntityEvent) ev;
				if (e1.getEntity() instanceof Player)
				{
					final Player p = (Player) e1.getEntity();
					final Player dmgr;
					if (e1.getDamager() instanceof Player)
						dmgr = (Player) e1.getDamager();
					else
						return false;
					if (p.getName() != AooniGameMainScripter.aooniPlayerName)
						if (dmgr.getName().equalsIgnoreCase(AooniGameMainScripter.aooniPlayerName))
							if (AooniGameMainScripter.instaKill)
								e1.setDamage(100000.0D);
				}
				break;
			case 1:
				final PlayerMoveEvent e2 = (PlayerMoveEvent) ev;
				if (e2.getPlayer().getName().equalsIgnoreCase(AooniGameMainScripter.aooniPlayerName))
					if (!AooniGameMainScripter.CanAooniMove)
						e2.setTo(e2.getFrom()); // FIXME
				break;
			case 2:
				final EntityDamageEvent e3 = (EntityDamageEvent) ev;

				if (e3.getEntity() instanceof Player)
					if (AooniGameMainScripter.GameScenario != GameStatus.NOPLAY)
						if (e3.getCause() != DamageCause.ENTITY_ATTACK)
							e3.setCancelled(true);
				break;
			case 3:
				final PlayerDeathEvent e4 = (PlayerDeathEvent) ev;
				if (AooniGameMainScripter.GameScenario == GameStatus.PLAY)
					if (!(e4 == null) && e4.getEntity() == null && e4.getEntity().getKiller() == null && e4.getEntity().getKiller().getName() == null && AooniGameMainScripter.aooniPlayerName == null)
						if (e4.getEntity().getKiller().getName().equalsIgnoreCase(AooniGameMainScripter.aooniPlayerName))
							Bukkit.broadcastMessage(AooniGameMainScripter.prefix() + ChatColor.LIGHT_PURPLE + e4.getEntity().getName() + "님이 죽었습니다.");
				break;
			case 4:
				final PlayerDropItemEvent e5 = (PlayerDropItemEvent) ev;
				if (AooniGameMainScripter.GameScenario == GameStatus.NOPLAY)
					return false;
				if (e5.getPlayer() != null)
				{
					final Item drop = e5.getItemDrop();
					final ItemStack item = drop.getItemStack();
					if (SkillManager.isGameItem(item))
						e5.setCancelled(true);
				}
				break;
			case 5:
				mainScripter.skillManager.InteractHook((PlayerInteractEvent) ev);
				break;
			case 6:
				mainScripter.skillManager.InventoryHook((InventoryClickEvent) ev);
				break;
			case 7:
				mainScripter.skillManager.EntityDamageByEntityEventHook((EntityDamageByEntityEvent) ev);
				break;
		}
		return false;
	}

	@Override
	public boolean onCommand(final CommandSender s, final String l, final String[] d)
	{
		if (l.equalsIgnoreCase(getModuleCommands()[0]))
			return mainScripter.handleCommand(s, l, d);
		return false;
	}
}
