package com.eric0210.magicswords.Items;

import java.util.logging.Level;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemType;
import com.eric0210.core.api.item.ItemTypeFactory;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;
import com.eric0210.magicswords.Magicswords;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ArrowRod extends ItemBase
{
	public ItemStack ArrowRod;

	@Override
	public void Init()
	{
		setItemName("ArrowRod");
		setItemCommand("arrowrod");
		ArrowRod = ItemUtils.createItem(Magicswords.instance.getModuleConfig().get("ArrowRod.Name", "하늘에서 화살이 내려와"), Material.STICK, 1, false, 1, 145, null);
		EventManager.onPlayerInteract.add(new EventData(this));
		ItemTypeFactory.add(new ItemType("ARROWROD", ArrowRod));
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String cmd, final String[] args)
	{
		if (sender.isOp() && cmd.equalsIgnoreCase(getItemCommand()) && sender instanceof Player)
		{
			final Player p = (Player) sender;
			p.getInventory().addItem(ArrowRod);
			p.updateInventory();
			return true;
		}
		return false;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		final PlayerInteractEvent e = (PlayerInteractEvent) ev;
		if (ItemTypeFactory.check(e.getPlayer().getItemInHand()) == "ARROWROD")
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				try
				{
					final double interval = Magicswords.instance.getModuleConfig().get("ArrowRod.large.period", 0.9D);
					final int size = Magicswords.instance.getModuleConfig().get("ArrowRod.large.size", 6);
					final int power = Magicswords.instance.getModuleConfig().get("ArrowRod.large.power", 4);

					final Player p = e.getPlayer();
					final Location l2 = GameUtils.getTargetBlock(p).getLocation();
					final Vector v = new Vector(0, -power, 0);
					l2.setY(256.0D);
					for (int i = -3; i <= size; i++)
						for (int j = -3; j <= size; j++)
						{
							final Location location;
							location = GameUtils.getTargetBlock(p).getLocation();
							location.setX(location.getX() + interval * i);
							if (location.getY() + interval > 250.0D)
								location.setY(250.0D);
							else
								location.setY(location.getY() + 10.0D);
							location.setZ(location.getZ() + interval * j);
							final Arrow arrow = p.getWorld().spawn(location, Arrow.class);
							arrow.setVelocity(v);
							arrow.setCritical(true);
							arrow.setShooter((ProjectileSource) p);
						}
					return true;
				}
				catch (final Exception ex)
				{
					EricPackage.PLUGIN_LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
				}
			else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
				try
				{
					final double interval = Magicswords.instance.getModuleConfig().get("ArrowRod.powerful.period", 0.5D);
					final int size = Magicswords.instance.getModuleConfig().get("ArrowRod.powerful.size", 8);
					final int power = Magicswords.instance.getModuleConfig().get("ArrowRod.powerful.power", 35);

					final Player p = e.getPlayer();
					final Location l2 = GameUtils.getTargetBlock(p).getLocation();
					final Vector v = new Vector(0, -power, 0);
					l2.setY(256.0D);
					for (int i = -3; i <= size; i++)
						for (int j = -3; j <= size; j++)
						{
							final Location location;
							location = GameUtils.getTargetBlock(p).getLocation();
							location.setX(location.getX() + interval * i);
							if (location.getY() + interval > 250.0D)
								location.setY(250.0D);
							else
								location.setY(location.getY() + 10.0D);
							location.setZ(location.getZ() + interval * j);
							final Arrow arrow = p.getWorld().spawn(location, Arrow.class);
							arrow.setVelocity(v);
							arrow.setCritical(true);
							arrow.setBounce(true);
							arrow.setShooter((ProjectileSource) p);
						}
					return true;
				}
				catch (final Exception ex)
				{
					EricPackage.PLUGIN_LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
				}
		return false;
	}
}
