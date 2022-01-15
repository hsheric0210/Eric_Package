package com.eric0210.magicswords.Items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemType;
import com.eric0210.core.api.item.ItemTypeFactory;
import com.eric0210.core.utils.ItemUtils;
import com.eric0210.core.utils.JavaHelper;
import com.eric0210.magicswords.Magicswords;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class Blaze extends ItemBase
{
	public ItemStack Blaze;
	Set<Integer> fireballs = new HashSet<>();
	Set<Integer> arrows = new HashSet<>();

	@Override
	public void Init()
	{
		setItemName("Blaze");
		setItemCommand("blaze");
		Blaze = ItemUtils.createItem(Magicswords.instance.getModuleConfig().get("Blaze.Name", "������������"), Material.BLAZE_ROD, 1, true, 1000, 142, Arrays.asList(ChatColor.STRIKETHROUGH + "����" + ChatColor.DARK_PURPLE + ChatColor.ITALIC.toString() + "ȭ������ ������.", "���� ū���� �˰� ���� ��"));
		EventManager.onPlayerInteract.add(new EventData(this, 0));
		EventManager.onProjectileHit.add(new EventData(this, 1));
		ItemTypeFactory.add(new ItemType("BLAZE", Blaze));
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String cmd, final String[] args)
	{
		if (sender.isOp() && cmd.equalsIgnoreCase(getItemCommand()) && sender instanceof Player)
		{
			final Player p = (Player) sender;
			p.getInventory().addItem(Blaze);
			p.updateInventory();
			return true;
		}
		return false;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		switch (eventDataIndex)
		{
			case 0:
			{
				final PlayerInteractEvent e = (PlayerInteractEvent) ev;
				if (ItemTypeFactory.check(e.getPlayer().getItemInHand()) == "BLAZE")
					if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
					{
						final Vector dir1 = e.getPlayer().getEyeLocation().getDirection().multiply(Magicswords.instance.getModuleConfig().get("Blaze.FireballVelocity", 1.8D));
						final Fireball fireball = e.getPlayer().getWorld().spawn(e.getPlayer().getEyeLocation().add(dir1.getX(), dir1.getY(), dir1.getZ()), Fireball.class);
						fireball.setVelocity(dir1);
						fireball.setYield(5.0F);
						fireball.setIsIncendiary(true);
						fireball.setShooter((ProjectileSource) e.getPlayer());
						fireballs.add(fireball.getEntityId());
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.GHAST_FIREBALL, 1.1F, 1.0F);
					}
					else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						for (int i = 0; i <= Magicswords.instance.getModuleConfig().get("Blaze.NumArrowMultiply", 10) + JavaHelper.random(5, 10); i++)
						{
							final Vector dir2 = e.getPlayer().getEyeLocation().getDirection().multiply(Magicswords.instance.getModuleConfig().get("Blaze.ArrowVelocity", 4.0D));
							final double spread = Magicswords.instance.getModuleConfig().get("Blaze.ArrowSpread", 0.25D);
							dir2.setX(JavaHelper.random(dir2.getX() - spread, dir2.getX() + spread));
							dir2.setY(JavaHelper.random(dir2.getY() - spread, dir2.getY() + spread));
							dir2.setZ(JavaHelper.random(dir2.getZ() - spread, dir2.getZ() + spread));
							final Arrow arrow = e.getPlayer().getWorld().spawn(e.getPlayer().getEyeLocation().add(dir2.getX(), dir2.getY(), dir2.getZ()), Arrow.class);
							arrow.setKnockbackStrength(Magicswords.instance.getModuleConfig().get("Blaze.ArrowKnockback", 15));
							arrow.setBounce(true);
							arrow.setFallDistance(100);
							arrow.setCritical(true);
							arrow.setFireTicks(10000);
							arrow.setVelocity(dir2);
							arrow.setShooter((ProjectileSource) e.getPlayer());
							arrows.add(arrow.getEntityId());
						}
						e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.SHOOT_ARROW, 1.0F, 1.0F);
					}
				break;
			}
			case 1:
			{
				final ProjectileHitEvent e3 = (ProjectileHitEvent) ev;
				if (e3.getEntityType() == EntityType.FIREBALL)
				{
					if (fireballs.contains(e3.getEntity().getEntityId()))
					{
						final Location loc = e3.getEntity().getLocation();
						loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 3.5F, true, true);
						loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0F, true, true);
					}
					fireballs.remove(e3.getEntity().getEntityId());
				}
				else if (e3.getEntityType() == EntityType.ARROW)
				{
					final Arrow arrow = (Arrow) e3.getEntity();
					if (arrows.contains(arrow.getEntityId()))
						Bukkit.getScheduler().scheduleSyncDelayedTask(EricPackage.instance(), () ->
						{
							arrow.remove();
							arrows.remove(arrow.getEntityId());
						}, 50L);
				}
			}
		}
		return false;
	}
}
