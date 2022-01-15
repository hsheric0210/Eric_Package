package com.eric0210.core;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eric0210.core.api.Module;
import com.eric0210.core.api.command.CommandBase;
import com.eric0210.core.api.command.CommandRegisterationException;
import com.eric0210.core.api.event.EventManager;
import com.eric0210.core.api.menu.ChestMenu;
import com.eric0210.core.api.menu.Menu;
import com.eric0210.core.utils.Registerer;
import com.eric0210.core.utils.TPSCalculator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class EricPackage extends JavaPlugin
{
	public static final Logger PLUGIN_LOGGER = Logger.getLogger("Eric_Package");
	private static EricPackage instance;
	public static final String PLUGIN_MESSAGE_FORMAT = "[" + ChatColor.RED + "Eric Package" + ChatColor.RESET + "] ";
	public PluginDescriptionFile desc = getDescription();
	public static File DEFAULT_MODULE_DATA_FOLDER;
	public static File DEFAULT_DATAFOLDER;
	public ChestMenu pluginmenu;
	private static boolean debugMode;

	// <editor-fold desc="onEnable">
	@Override
	public void onEnable()
	{
		instance = this;

		PLUGIN_LOGGER.info("[Eric Package] Initializing Config...");

		try
		{
			DEFAULT_DATAFOLDER = getDataFolder();

			// config.yml
			if (!new File(DEFAULT_DATAFOLDER, "config.yml").exists())
				saveDefaultConfig();
			reloadConfig();

			// Module configurations
			final File moduleConfigDir = new File(DEFAULT_DATAFOLDER, "modules");
			if (!moduleConfigDir.exists())
				moduleConfigDir.mkdirs();

			DEFAULT_MODULE_DATA_FOLDER = moduleConfigDir;

			// TODO: Initialize module config files
		}
		catch (final RuntimeException e)
		{
			logException("Unable to load configurations", e);
		}

		PLUGIN_LOGGER.info("[Eric Package] Initializing EventManager..." + runAndGetResult(() -> getServer().getPluginManager().registerEvents(new EventManager(), instance)));

		PLUGIN_LOGGER.info("[Eric Package] Initializing Modules..." + runAndGetResult(() ->
		{
			Module.loadall();
		}));

		try
		{
			Registerer.RegisterCommand(new DefaultCommands());
		}
		catch (final CommandRegisterationException e)
		{
			logException("Unable to register commands", e);
		}

		PLUGIN_LOGGER.info("[Eric Package] Initializing PacketManager...");
		TPSCalculator.initialize();
	}
	// </editor-fold>

	// <editor-fold desc="onDisable">
	@Override
	public void onDisable()
	{
		try
		{
			Menu.closeAll();
			PLUGIN_LOGGER.info("[Eric Package] Disabling Modules...");
			Module.unloadall();
		}
		catch (final Throwable t)
		{
			logException("Unable to disable the plugin", t);
		}
	}
	// </editor-fold>

	// <editor-fold desc="reloadConfig">
	@Override
	public final void reloadConfig()
	{
		super.reloadConfig();
		debugMode = getConfig().getBoolean("Debug", false);
	}
	// </editor-fold>

	public class DefaultCommands extends CommandBase
	{
		public DefaultCommands()
		{
			addCommand("ericpackage");
		}

		@Override
		public boolean onCommand(final CommandSender s, final String cmd, final String[] args)
		{
			if (!s.isOp())
				return false;

			if (cmd.equalsIgnoreCase("ericpackage"))
			{
				// FIXME: Fix everything
				pluginmenu = new ChestMenu("Eric's Package Plugin Configuration");

				pluginmenu.setButton(new ItemStack(Material.ENCHANTMENT_TABLE), "占쏙옙占쏙옙 占쏙옙琯占�", 13, Arrays.asList("占시뤄옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쌕쏙옙 占싸듸옙占쌌니댐옙"), new ChestMenu.Run()
				{

					@Override
					public void run(final Inventory inventory, final Player p)
					{
						reloadConfig();
						Module.reloadConfigAll();
						p.sendMessage(ChatColor.GREEN + "All modules config are been reloaded.");
					}
				});
				pluginmenu.open((Player) s);
			}
			return false;
		}

	}

	public static final EricPackage instance()
	{
		return instance;
	}

	private String runAndGetResult(final Runnable work)
	{
		try
		{
			work.run();
		}
		catch (final Throwable e)
		{
			return "Failed: " + e.toString();
		}

		return "Done";
	}

	public static final void debug(final String message)
	{
		if (debugMode)
			PLUGIN_LOGGER.log(Level.INFO, String.format("[Eric Package] [DEBUG] %s", message));
	}

	public static final void info(final String message)
	{
		PLUGIN_LOGGER.info("[Eric Package] " + message);
	}

	public static final void warning(final String message)
	{
		PLUGIN_LOGGER.warning("[Eric Package] " + message);
	}

	public static final void logException(final String message, final Throwable thrown)
	{
		final StackTraceElement[] trace = Thread.currentThread().getStackTrace();

		final String fixedMessage;
		if (message != null && message.isEmpty())
			fixedMessage = message + ":";
		else
			fixedMessage = "";

		final String exceptionDetails = thrown.toString();
		final StackTraceElement caller = trace[2];

		final String logRecord = String.format("[Eric Package] %s '%s' at '%s'", fixedMessage, exceptionDetails, caller); /* example: Failed to handle command: java.lang.NullPointerException: null at EricPackage.class(Unknown Source) */
		PLUGIN_LOGGER.log(Level.SEVERE, logRecord, thrown);
	}
}
