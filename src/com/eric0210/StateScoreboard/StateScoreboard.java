package com.eric0210.StateScoreboard;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import com.eric0210.core.api.Module;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.utils.GameUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

public class StateScoreboard extends Module
{
	private static final HashMap<Player, StatsBoard> REGISTRY = new HashMap<>();

	@Override
	public final void load()
	{
		EventManager.onPlayerJoin.add(new EventData(this, 0));

		for (final Player player : Objects.requireNonNull(GameUtils.getOnlinePlayers()))
		{
			final StatsBoard board = new StatsBoard(player);
			REGISTRY.put(player, board);
			board.setUpdaterRunning(true);
		}
	}

	@Override
	public final void unload()
	{
		for (final Entry<Player, StatsBoard> entry : REGISTRY.entrySet())
			if (REGISTRY.containsKey(entry.getKey()))
			{
				final StatsBoard sb = entry.getValue();
				sb.setUpdaterRunning(false);
			}
	}

	public static final void setState(final boolean enabled)
	{
		for (final Entry<Player, StatsBoard> entry : REGISTRY.entrySet())
			if (REGISTRY.containsKey(entry.getKey()))
			{
				final StatsBoard sb = entry.getValue();
				sb.setUpdaterRunning(enabled);
			}
	}

	@Override
	public final boolean onEvent(final Event ev, final int eventDataIndex)
	{
		final PlayerJoinEvent e2 = (PlayerJoinEvent) ev;
		final Player p = e2.getPlayer();
		final StatsBoard sb = new StatsBoard(p);
		REGISTRY.put(p, sb);
		return true;
	}
}
