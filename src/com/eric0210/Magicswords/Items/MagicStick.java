package com.eric0210.magicswords.Items;

import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.item.ItemBase;
import com.eric0210.core.api.item.ItemType;
import com.eric0210.core.api.item.ItemTypeFactory;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MagicStick extends ItemBase
{
	private ItemStack ms;

	@Override
	public void Init()
	{
		setItemName("매직스틱");
		setItemCommand("매직스틱");
		final String name = ChatColor.RED + "매" + ChatColor.YELLOW + "직" + ChatColor.GREEN + "스" + ChatColor.BLUE + "틱";
		EventManager.onPlayerInteract.add(new EventData(this));
		ms = ItemUtils.createItem(name, Material.TORCH, 64, true, 1666, 4444, null);
		ItemTypeFactory.add(new ItemType("MAGICSTICK", ms));
	}

	@Override
	public boolean onCommand(final CommandSender s, final String cmd, final String[] args)
	{
		if (s.isOp() && cmd.equalsIgnoreCase(getItemCommand()) && s instanceof Player)
			if (GameUtils.isPlayer(s))
			{
				final Player p = (Player) s;
				p.getInventory().addItem(ms);
				return true;
			}
		return false;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		final PlayerInteractEvent e = (PlayerInteractEvent) ev;
		if (e.getPlayer() != null)
		{
			final Player p = e.getPlayer();
			if (p.getItemInHand() != null && ItemTypeFactory.check(p.getItemInHand()) == "MAGICSTICK")
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					final Block b = e.getClickedBlock();
					if (b != null && b.getType() != Material.AIR)
					{
						GameUtils.sphereEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, null, 150, 1.5);
						final Entity[] ents = GameUtils.findInSphere(b.getLocation(), 5.5D);
						for (final Entity ent : ents)
						{
							if (ent instanceof Player && (ent.equals(p) || ((HumanEntity) ent).getGameMode() == GameMode.CREATIVE))
								continue;

							if (ent instanceof Damageable)
								((Damageable) ent).damage(99999.9D);
							else
								ent.remove();
							GameUtils.sphereEffect(ent.getLocation(), Effect.EXPLOSION_LARGE, null, 30, 1);
						}
					}
				}
		}
		return false;
	}
}
