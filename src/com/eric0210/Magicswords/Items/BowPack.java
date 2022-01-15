package com.eric0210.magicswords.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemType;
import com.eric0210.core.api.item.ItemTypeFactory;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;
import com.eric0210.core.utils.JavaHelper;
import com.eric0210.magicswords.Magicswords;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class BowPack extends ItemBase
{
	public ItemStack Bow;
	public ItemStack Arrow;

	@Override
	public void Init()
	{

		setItemName("BowPack");
		setItemCommand("bow");

		final List<String> Bow_Lore = new ArrayList<>();
		Bow_Lore.add(ChatColor.RESET.toString() + ChatColor.DARK_PURPLE + "갓-활" + ChatColor.RESET);
		Bow_Lore.add(ChatColor.RESET.toString() + ChatColor.DARK_PURPLE + "갓-화살" + ChatColor.RESET);
		EventManager.onProjectileLaunch.add(new EventData(this));
		Bow = ItemUtils.createItem(Magicswords.instance.getModuleConfig().get("Bow.Name", "�Ƿ����� Ȱ"), Material.BOW, 1, true, 1000, 733, Bow_Lore);
		Arrow = ItemUtils.createItem(Magicswords.instance.getModuleConfig().get("Arrow.Name", "�Ƿ����� ȭ��"), Material.ARROW, 64, true, 1000, 312, null);
		ItemTypeFactory.add(new ItemType("BowPack", Bow));
	}

	@Override
	public boolean onCommand(final CommandSender sender, final String cmd, final String[] args)
	{
		if (sender.isOp() && cmd.equalsIgnoreCase(getItemCommand()) && sender instanceof Player)
			if (GameUtils.isPlayer(sender))
			{
				final Player p = (Player) sender;
				p.getInventory().addItem(Bow, Arrow);
				p.updateInventory();
				return true;
			}
		return false;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		final ProjectileLaunchEvent e = (ProjectileLaunchEvent) ev;
		if (e.getEntityType() == EntityType.ARROW && e.getEntity() instanceof Arrow)
		{
			final Arrow arrow = (Arrow) e.getEntity();
			final Vector vel = arrow.getVelocity();
			if (arrow.getShooter() != null && arrow.getShooter() instanceof Player)
			{
				final Player shooter = (Player) arrow.getShooter();
				if (shooter.getItemInHand() != null && ItemTypeFactory.check(shooter.getItemInHand()) == "BowPack")
					for (int i = 0; i <= 5 + new Random().nextInt(8); i++)
					{
						final double spread = 0.2;
						final Vector randomVec = new Vector(JavaHelper.random(vel.getX() - spread, vel.getX() + spread), JavaHelper.random(vel.getY() - spread, vel.getY() + spread), JavaHelper.random(vel.getZ() - spread, vel.getZ() + spread));
						final Arrow arrow2 = shooter.getWorld().spawn(arrow.getLocation(), Arrow.class);
						arrow2.setVelocity(randomVec);
						arrow2.setBounce(arrow.doesBounce());
						arrow2.setCritical(arrow.isCritical());
						arrow2.setFireTicks(arrow.getFireTicks());
						arrow2.setShooter((ProjectileSource) shooter);
					}
			}
			return true;
		}

		return false;
	}
}
