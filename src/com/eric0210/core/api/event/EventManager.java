package com.eric0210.core.api.event;

import java.util.ArrayList;
import java.util.List;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandManager;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class EventManager implements Listener
{
	public static ArrayList<EventData> onEntityDamage = new ArrayList<>();
	public static ArrayList<EventData> onEntityDamageByEntity = new ArrayList<>();
	public static ArrayList<EventData> onEntityDamageByBlock = new ArrayList<>();

	@EventHandler
	public void onEntityDamage(final EntityDamageEvent e)
	{
		dispatchEvent(onEntityDamage, e);
		if (e instanceof EntityDamageByEntityEvent)
			dispatchEvent(onEntityDamageByEntity, e);

		if (e instanceof EntityDamageByBlockEvent)
			dispatchEvent(onEntityDamageByBlock, e);
	}

	public static ArrayList<EventData> onEntityDeath = new ArrayList<>();

	public static ArrayList<EventData> onPlayerDeath = new ArrayList<>();

	@EventHandler
	public void onEntityDeath(final EntityDeathEvent e)
	{
		dispatchEvent(onEntityDeath, e);
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent e)
	{
		dispatchEvent(onPlayerDeath, e);
	}

	public static ArrayList<EventData> onEntityTarget = new ArrayList<>();

	@EventHandler
	public void onEntityTarget(final EntityTargetEvent e)
	{
		dispatchEvent(onEntityTarget, e);
	}

	public static ArrayList<EventData> onFoodLevelChange = new ArrayList<>();

	@EventHandler
	public void onFoodLevelChangeEvent(final FoodLevelChangeEvent e)
	{

		dispatchEvent(onFoodLevelChange, e);
	}

	public static ArrayList<EventData> onPlayerTeleport = new ArrayList<>();

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent e)
	{
		dispatchEvent(onPlayerTeleport, e);
	}

	public static ArrayList<EventData> onEntityRegainHealth = new ArrayList<>();

	@EventHandler
	public void onEntityRegainHealth(final EntityRegainHealthEvent e)
	{
		dispatchEvent(onEntityRegainHealth, e);
	}

	public static ArrayList<EventData> onPlayerDropItem = new ArrayList<>();

	@EventHandler
	public void onPlayerDropItem(final PlayerDropItemEvent e)
	{
		dispatchEvent(onPlayerDropItem, e);
	}

	public static ArrayList<EventData> onPlayerRespawn = new ArrayList<>();

	@EventHandler
	public void onPlayerRespawnEvent(final PlayerRespawnEvent e)
	{
		dispatchEvent(onPlayerRespawn, e);
	}

	public static ArrayList<EventData> onPlayerInteract = new ArrayList<>();

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent e)
	{
		dispatchEvent(onPlayerInteract, e);
	}

	public static ArrayList<EventData> onPlayerMove = new ArrayList<>();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMoveEvent(final PlayerMoveEvent e)
	{
		dispatchEvent(onPlayerMove, e);
	}

	public static ArrayList<EventData> onProjectileHit = new ArrayList<>();

	@EventHandler
	public void onProjectileHit(final ProjectileHitEvent e)
	{
		dispatchEvent(onProjectileHit, e);
	}

	public static ArrayList<EventData> onProjectileLaunch = new ArrayList<>();

	@EventHandler
	public void onProjectileLaunch(final ProjectileLaunchEvent e)
	{
		dispatchEvent(onProjectileLaunch, e);
	}

	public static ArrayList<EventData> onBlockPlace = new ArrayList<>();

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent e)
	{
		dispatchEvent(onBlockPlace, e);
	}

	public static ArrayList<EventData> onSignChange = new ArrayList<>();

	@EventHandler
	public void onSignChange(final SignChangeEvent e)
	{
		dispatchEvent(onSignChange, e);
	}

	public static ArrayList<EventData> onBlockBreak = new ArrayList<>();

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e)
	{
		dispatchEvent(onBlockBreak, e);
	}

	public static ArrayList<EventData> onBlockDamage = new ArrayList<>();

	@EventHandler
	public void onBlockDamage(final BlockDamageEvent e)
	{

		dispatchEvent(onBlockDamage, e);
	}

	public static ArrayList<EventData> onPlayerItemConsume = new ArrayList<>();

	@EventHandler
	public void onPlayerItemConsume(final PlayerItemConsumeEvent e)
	{
		dispatchEvent(onPlayerItemConsume, e);
	}

	public static ArrayList<EventData> onPlayerJoin = new ArrayList<>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(final PlayerJoinEvent e)
	{
		dispatchEvent(onPlayerJoin, e);
	}

	public static ArrayList<EventData> onPlayerQuit = new ArrayList<>();

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e)
	{
		dispatchEvent(onPlayerQuit, e);
	}

	public static ArrayList<EventData> onPlayerInteractEntity = new ArrayList<>();

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent e)
	{
		dispatchEvent(onPlayerInteractEntity, e);
	}

	public static ArrayList<EventData> onPlayerCommandPreProcess = new ArrayList<>();

	@EventHandler
	public void onPlayerCommandPreProcess(final PlayerCommandPreprocessEvent e)
	{
		dispatchEvent(onPlayerCommandPreProcess, e);
		if (CommandManager.containsCommand(e.getMessage()))
		{
			CommandManager.dispatch(e.getPlayer(), e.getMessage());
			e.setCancelled(true);
		}

	}

	public static ArrayList<EventData> onServerCommand = new ArrayList<>();

	@EventHandler
	public void onServerCommand(final ServerCommandEvent e)
	{
		dispatchEvent(onServerCommand, e);
		if (CommandManager.containsCommand(e.getCommand()))
		{
			CommandManager.dispatch(e.getSender(), e.getCommand());
			e.setCommand("ericpackagecommand");
		}
	}

	public static ArrayList<EventData> onRemoteServerCommand = new ArrayList<>();

	@EventHandler
	public void onRemoteServerCommand(final RemoteServerCommandEvent e)
	{
		dispatchEvent(onRemoteServerCommand, e);
		if (CommandManager.containsCommand(e.getCommand()))
		{
			CommandManager.dispatch(e.getSender(), e.getCommand());
			e.setCommand("ericpackagecommand");
		}
	}

	public static ArrayList<EventData> onAsyncPlayerChat = new ArrayList<>();

	@EventHandler
	public void onAsyncPlayerChat(final AsyncPlayerChatEvent e)
	{
		dispatchEvent(onAsyncPlayerChat, e);
	}

	public static ArrayList<EventData> onPlayerKick = new ArrayList<>();

	@EventHandler
	public void onPlayerKick(final PlayerKickEvent e)
	{
		dispatchEvent(onPlayerKick, e);
	}

	public static ArrayList<EventData> onInventoryClick = new ArrayList<>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(final InventoryClickEvent e)
	{
		dispatchEvent(onInventoryClick, e);
	}

	public static ArrayList<EventData> onInventoryPickupItem = new ArrayList<>();

	@EventHandler
	public void onInventoryPickupItem(final InventoryPickupItemEvent e)
	{
		dispatchEvent(onInventoryPickupItem, e);
	}

	public static ArrayList<EventData> onInventoryInteract = new ArrayList<>();

	@EventHandler
	public void onInventoryInteract(final InventoryInteractEvent e)
	{

		dispatchEvent(onInventoryInteract, e);
	}

	public static ArrayList<EventData> onPlayerLogin = new ArrayList<>();

	@EventHandler
	public void onPlayerLogin(final PlayerLoginEvent e)
	{

		dispatchEvent(onPlayerLogin, e);
	}

	public static ArrayList<EventData> onPlayerPortal = new ArrayList<>();

	@EventHandler
	public void onPlayerPortal(final PlayerPortalEvent e)
	{
		dispatchEvent(onPlayerPortal, e);
	}

	public static ArrayList<EventData> onPlayerLevelChange = new ArrayList<>();

	@EventHandler
	public void onPlayerLevelChange(final PlayerLevelChangeEvent e)
	{
		dispatchEvent(onPlayerLevelChange, e);
	}

	public static ArrayList<EventData> onPlayerExpChange = new ArrayList<>();

	@EventHandler
	public void onPlayerExpChange(final PlayerExpChangeEvent e)
	{
		dispatchEvent(onPlayerExpChange, e);
	}

	public static ArrayList<EventData> onPlayerEmptyBucket = new ArrayList<>();

	@EventHandler
	public void onPlayerEmptyBucket(final PlayerBucketEmptyEvent e)
	{
		dispatchEvent(onPlayerEmptyBucket, e);
	}

	public static ArrayList<EventData> onPlayerFillBucket = new ArrayList<>();

	@EventHandler
	public void onPlayerFillBucket(final PlayerBucketFillEvent e)
	{
		dispatchEvent(onPlayerFillBucket, e);
	}

	public static ArrayList<EventData> onPlayerEditBook = new ArrayList<>();

	@EventHandler
	public void onEditBook(final PlayerEditBookEvent e)
	{
		dispatchEvent(onPlayerEditBook, e);
	}

	public static ArrayList<EventData> onPlayerFish = new ArrayList<>();

	@EventHandler
	public void onPlayerFish(final PlayerFishEvent e)
	{
		dispatchEvent(onPlayerFish, e);
	}

	public static ArrayList<EventData> onEntityShootBow = new ArrayList<>();

	@EventHandler
	public void onEntityShootBow(final EntityShootBowEvent e)
	{
		dispatchEvent(onEntityShootBow, e);
	}

	public static ArrayList<EventData> onEntityExplode = new ArrayList<>();

	@EventHandler
	public void onEntityExplode(final EntityExplodeEvent e)
	{
		dispatchEvent(onEntityExplode, e);
	}

	public static ArrayList<EventData> onPlayerItemHeld = new ArrayList<>();

	@EventHandler
	public void onPlayerItemHeld(final PlayerItemHeldEvent e)
	{
		dispatchEvent(onPlayerItemHeld, e);
	}

	private static void dispatchEvent(final List<EventData> eventHandlers, final Event event) // FIXME: Optimize
	{
		try
		{
			for (final EventData handler : eventHandlers)
			{
				if (handler.base != null)
				{
					if (handler.base instanceof Module)
					{
						final Module module = (Module) handler.base;

						// If the handler module is disabled, Skip it.
						if (!module.isModuleEnabled())
							continue;
					}
					handler.base.ExecuteEvent(event, handler.parameter);
				}

				if (handler.eventRunner != null)
					handler.eventRunner.run(event);
			}
		}
		catch (final Throwable t)
		{
			EricPackage.logException("Unable to handle event " + event.getClass().getSimpleName(), t);
		}
	}
}
