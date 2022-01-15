package com.eric0210.AdvFilters;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandManager;
import com.eric0210.core.api.event.EventData;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.utils.GameUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;

public class AdvFilters extends Module
{
	public static final String FILTERED_CHAT_WORDS_LIST_FILENAME = "FilteredWords.txt";
	public static final String FILTERED_CHAT_WORDS_LIST_FILE_NOTE = "# All words will be separated by a new line #";

	public static final String FILTERED_ITEMS_LIST_FILENAME = "FilteredItems.txt";
	public static final String FILTERED_ITEMS_LIST_FILE_NOTE = "# <material|material id>:<data(a.k.a. durability, damage) format (ex: Stone:0) #";

	private static AdvFilters instance;

	public ChatFilter chatFilter;
	public ItemFilter itemFilter;
	private String CommandBlockedMessagePlayer;
	private String CommandBlockedMessageConsole;
	private String ItemBlockedMessage;

	private List<String> filteredChatWords;
	private List<String> filteredItems;

	@Override
	public void load()
	{
		instance = this;

		CommandBlockedMessagePlayer = getModuleConfig().get("BlockedCommandInfo_Player", ChatColor.RED + "This command is not available.");
		CommandBlockedMessageConsole = getModuleConfig().get("BlockedCommand_Console", "This command is not available.");
		ItemBlockedMessage = getModuleConfig().get("BlockedItemInfo", ChatColor.RED + "This item is not available.");

		getModuleConfig().get("BlockedCommands.ExampleBlockedCommand.player", true);
		getModuleConfig().get("BlockedCommands.ExampleBlockedCommand.console", true);

		if (getModuleConfig().getNoCreate("CensorWith") == null)
			getModuleConfig().setList("CensorWith", Arrays.asList('!', '#', '$', '%', '*'));

		filteredChatWords = readFilteredWords(new File(getDataFolder(), FILTERED_CHAT_WORDS_LIST_FILENAME), FILTERED_CHAT_WORDS_LIST_FILE_NOTE);
		filteredItems = readFilteredItems(new File(getDataFolder(), FILTERED_ITEMS_LIST_FILENAME), FILTERED_ITEMS_LIST_FILE_NOTE);

		EventManager.onPlayerCommandPreProcess.add(new EventData(this, 0));
		EventManager.onServerCommand.add(new EventData(this, 1));
		EventManager.onRemoteServerCommand.add(new EventData(this, 2));
		EventManager.onPlayerInteract.add(new EventData(this, 3));
		EventManager.onAsyncPlayerChat.add(new EventData(this, 4));

		CommandManager.registerHandler(this, "ctrl", "control", "playercontrol");

		chatFilter = new ChatFilter();
		itemFilter = new ItemFilter();
	}

	private List<String> readFilteredWords(final File file, final String defaultNote)
	{
		final List<String> filteredWords;

		if (file == null)
			return null;

		if (!file.exists())
		{
			try (final BufferedWriter bWriter = new BufferedWriter(new FileWriter(file)))
			{
				if (file.createNewFile())
					bWriter.write(defaultNote);
			}
			catch (final IOException ioe)
			{
				EricPackage.logException("Can't write default filtered words list file (" + file + ")", ioe);
				return null;
			}
		}

		try (final BufferedReader bReader = new BufferedReader(new FileReader(file)))
		{
			filteredWords = bReader.lines().parallel().filter(line -> !line.isEmpty() && line.charAt(0) != '#').collect(Collectors.toList());
		}
		catch (final IOException ioe)
		{
			EricPackage.logException("Can't load filtered words list file (" + file + ")", ioe);
			return null;
		}

		return filteredWords;
	}

	private List<String> readFilteredItems(final File file, final String defaultNote)
	{
		final List<String> filteredItems;

		if (file == null)
			return null;

		if (!file.exists())
		{
			try (final BufferedWriter bWriter = new BufferedWriter(new FileWriter(file)))
			{
				if (file.createNewFile())
					bWriter.write(defaultNote);
			}
			catch (final IOException ioe)
			{
				EricPackage.logException("Can't write default filtered items list file (" + file + ")", ioe);
				return null;
			}
		}

		try (final BufferedReader bReader = new BufferedReader(new FileReader(file)))
		{
			filteredItems = bReader.lines().parallel().filter(line -> !line.isEmpty() && line.charAt(0) != '#').collect(Collectors.toList());
		}
		catch (final IOException ioe)
		{
			EricPackage.logException("Can't load filtered items list file (" + file + ")", ioe);
			return null;
		}

		return filteredItems;
	}

	@Override
	public void unload()
	{
		chatFilter = null;
		itemFilter = null;
	}

	@Override
	public boolean onEvent(final Event ev, final int eventDataIndex)
	{
		switch (eventDataIndex)
		{
			case 0:
				final PlayerCommandPreprocessEvent e1 = (PlayerCommandPreprocessEvent) ev;
				debug("Player ", e1.getPlayer().getName() + " entered command \"" + e1.getMessage() + "\"");
				if (!CommandFilter.checkCommand(e1.getMessage(), e1.getPlayer()))
				{
					e1.setCancelled(true);
					e1.getPlayer().sendMessage(CommandBlockedMessagePlayer);
				}
				break;
			case 1:
				final ServerCommandEvent e2 = (ServerCommandEvent) ev;
				debug("Server Console entered command \"", e2.getCommand() + "\"");
				if (!CommandFilter.checkCommand(e2.getCommand(), e2.getSender()))
				{
					e2.setCommand("");
					e2.getSender().sendMessage(CommandBlockedMessageConsole);
				}
				break;
			case 2:
				final RemoteServerCommandEvent e3 = (RemoteServerCommandEvent) ev;
				debug("Remote Server command", e3.getCommand());
				if (!CommandFilter.checkCommand(e3.getCommand(), e3.getSender()))
				{
					e3.setCommand("");
					e3.getSender().sendMessage(CommandBlockedMessageConsole);
				}
				break;
			case 3:
				final PlayerInteractEvent e4 = (PlayerInteractEvent) ev;
				if (e4.getPlayer() != null)
				{
					final ItemStack heldItem = e4.getPlayer().getItemInHand();
					if (heldItem != null)
					{
						if (itemFilter.isBlocked(heldItem))
						{
							debug("Block item", heldItem.getType() + " blocked for " + e4.getPlayer().getName());

							e4.setCancelled(true);
							e4.getPlayer().sendMessage(ItemBlockedMessage);
						}
					}
				}
				break;
			case 4:
				final AsyncPlayerChatEvent e5 = (AsyncPlayerChatEvent) ev;
				final String originalChat = e5.getMessage();
				final Player p = e5.getPlayer();
				final String filteredChat = chatFilter.check(p, originalChat);
				if (!originalChat.equals(filteredChat))
				{
					final ChatFilterEvent filterEvent = new ChatFilterEvent(p, originalChat, filteredChat);
					Bukkit.getPluginManager().callEvent(filterEvent);

					if (!filterEvent.isCancelled())
						e5.setMessage(filteredChat);
				}
				break;
		}
		return false;
	}

	@Override
	public boolean onCommand(final CommandSender s, final String l, final String[] d)
	{
		if (s.isOp())
		{
			if (d.length > 0)
			{
				final Player p = Bukkit.getPlayer(d[0]);
				if (p != null && GameUtils.isPlayer(s))
				{
					Menu.PlayerMenu.openMenuTo((Player) s, p);
					s.sendMessage(ChatColor.GREEN + "Sucessfully opened the control menu of " + p.getName());
					return true;
				}
				s.sendMessage(ChatColor.RED + "Cannot fine the specified player in this server (" + d[0] + ")");
			}
			s.sendMessage(ChatColor.RED + "/<ctrl|control|playercontrol> <playername|unban> [playername to unban]");
		}
		return false;
	}

	public void debug(final String k, final Object v)
	{
		EricPackage.debug(String.format("%s : %s", k, v));
	}

	public static AdvFilters instance()
	{
		return instance;
	}

	public List<Character> getCensorWith()
	{
		return getModuleConfig().getListNoCreate("CensorWith", Character.class);
	}

	public List<String> getFilteredWords()
	{
		return filteredChatWords;
	}

	public List<String> getFilteredItems()
	{
		return filteredItems;
	}
}
