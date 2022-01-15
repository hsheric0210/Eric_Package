package com.eric0210.AdditionalFunctions;

import com.eric0210.core.api.ChatTask;
import com.eric0210.core.api.IEventHandler;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AdditionalMessages implements IEventHandler
{

	@Override
	public boolean onEvent(final Event event, final int eventDataIndex)
	{
		switch (eventDataIndex)
		{
			case 0:
			{
				final PlayerJoinEvent e = (PlayerJoinEvent) event;
				if (e.getPlayer() != null)
					if (e.getJoinMessage() != null)
						e.setJoinMessage(String.format("%s%s", StringUtils.getBracketPrefix(ChatColor.GREEN, "+"), e.getPlayer().getDisplayName()));
				break;
			}
			case 1:
			{
				final PlayerQuitEvent e = (PlayerQuitEvent) event;
				if (e.getPlayer() != null)
					if (e.getQuitMessage() != null)
						e.setQuitMessage(String.format("%s%s", StringUtils.getBracketPrefix(ChatColor.RED, "-"), e.getPlayer().getDisplayName()));
				break;
			}
			case 2:
			{
				final PlayerKickEvent e = (PlayerKickEvent) event;
				if (e.getPlayer() != null)
					if (e.getLeaveMessage() != null)
						e.setLeaveMessage(String.format("%s%s (%s)", StringUtils.getBracketPrefix(ChatColor.RED, "-"), e.getPlayer().getDisplayName(), e.getReason()));
				break;
			}
			case 3:
			{
				final AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
				if (e.getPlayer() != null && !e.isCancelled())
				{
					e.setFormat(StringUtils.getBracketPrefix(StringUtils.randomChatColor(), "Chat") + " %1$s" + ChatColor.RESET + "> %2$s");
					if (ChatTask.onChat(e.getMessage())) // FIXME: Chat filter. Move it to AdvPermissions
						return true;
				}
				break;
			}
			case 6:
			{
				final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				if (e.getEntity() != null && e.getDamager() != null)
					if (GameUtils.isPlayer(e.getEntity()) && e.getDamager().getType() == EntityType.ARROW)
					{
						final Player victim = (Player) e.getEntity();
						final Arrow arrow = (Arrow) e.getDamager();

						if (arrow.getShooter() != null && GameUtils.isPlayer(arrow.getShooter()))
						{
							final Player attacker = (Player) arrow.getShooter();
							final String victimName = victim.getName() == attacker.getName() ? "Yourself" : victim.getName();
							attacker.sendMessage(StringUtils.getBracketPrefix(ChatColor.GREEN, "Shot") + ChatColor.GREEN + String.format("Your bow shot hit %s and totally gave %.2f damage(s); distance: %.3f block(s)", victimName, e.getFinalDamage(), victim.getLocation().distance(attacker.getLocation())));
						}
						return true;
					}
				break;
			}
		}
		return false;
	}
}
