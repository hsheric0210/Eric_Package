package com.eric0210.core.api;

import java.io.File;
import java.util.HashMap;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.command.ICommandHandler;
import com.eric0210.core.api.command.CommandManager;
import com.eric0210.core.config.Config;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;

public abstract class Module extends EventHandler implements ICommandHandler, Loadable
{

	public static HashMap<String, Module> moduleMapping = new HashMap<>();
	private static final transient FileConfiguration config = EricPackage.instance().getConfig();
	private static final String CONFIG_MODULE_MAIN_DIR = ".moduleMain";

	String[] moduleCommands;
	Config moduleConfig;
	boolean moduleEnabled;
	File moduleDataFolder;

	// <editor-fold desc="Getter/Setters">
	public final void setModuleEnabled(final boolean b)
	{
		moduleEnabled = b;
	}

	private void setModuleDataFolder(final File moduleDir)
	{
		moduleDataFolder = moduleDir;
	}

	public final File getDataFolder()
	{
		return moduleDataFolder;
	}

	public final boolean isModuleEnabled()
	{
		return moduleEnabled;
	}

	public final void setModuleConfig(final Config conf)
	{
		moduleConfig = conf;
	}

	public final Config getModuleConfig()
	{
		return moduleConfig;
	}

	public final String getModuleName()
	{
		return getClass().getSimpleName();
	}

	public final void setModuleCommands(final String... commands)
	{
		moduleCommands = commands;
		CommandManager.registerHandler(this, commands);
	}

	public final String[] getModuleCommands()
	{
		return moduleCommands;
	}
	// </editor-fold>

	public static final Module getByName(final String moduleName)
	{
		if (moduleMapping.get(moduleName) != null)
			return moduleMapping.get(moduleName);
		return null;
	}

	// <editor-fold desc="Module load/unload">
	public static final void loadall()
	{
		for (final String configKey : config.getKeys(false))
		{
			// Config key which contains '.moduleMain' directory is a module config key
			final String moduleMain = config.getString(configKey + CONFIG_MODULE_MAIN_DIR);
			if (moduleMain != null)
				try
				{
					final Class<? extends Module> clazz = (Class<? extends Module>) Class.forName(moduleMain);

					final Module module = clazz.newInstance();
					final String moduleName = configKey;

					moduleMapping.put(moduleName, module);

					final File moduleDataFolder = new File(EricPackage.DEFAULT_MODULE_DATA_FOLDER, moduleName);
					if (!moduleDataFolder.exists())
						moduleDataFolder.mkdirs();
					module.setModuleDataFolder(moduleDataFolder);

					module.setModuleConfig(new Config(moduleName, moduleDataFolder));

					module.setModuleEnabled(true);

					module.load();

					EricPackage.info("[Eric Package] Module '" + moduleName + "' loaded");
				}
				catch (final ClassCastException e)
				{
					EricPackage.logException("The module doesn't extends 'com.eric0210.core.api.Module' class", e);
				}
				catch (final Exception e)
				{
					EricPackage.logException("Unable to load module", e);
				}
		}
	}

	public static final void reloadConfigAll()
	{
		for (final String moduleName : moduleMapping.keySet())
			try
			{
				final Module module = getByName(moduleName);
				if (module != null)
				{
					final File moduleDataFolder = new File(EricPackage.DEFAULT_MODULE_DATA_FOLDER, moduleName);
					if (!moduleDataFolder.exists())
						moduleDataFolder.mkdirs();
					module.setModuleConfig(new Config(moduleName, moduleDataFolder));

					EricPackage.info(String.format("[Eric Package] Module %s's config file path is %s", moduleName, module.getModuleConfig().getFile().getPath()));
					module.load();
					EricPackage.info(String.format("[Eric Package] Module %s config is reloaded", moduleName));
				}
			}
			catch (final NullPointerException t)
			{
				EricPackage.logException("Unable to reload all module configurations", t);
			}
	}

	public static final void load(final String moduleName)
	{
		final String moduleMain = config.getString(moduleName + CONFIG_MODULE_MAIN_DIR);
		if (moduleMain != null)
			try
			{
				final Module currentModule = getByName(moduleName);
				moduleMapping.put(moduleName, currentModule);
				currentModule.setModuleEnabled(true);
				currentModule.load();

				EricPackage.info(String.format("[Eric Package] Function %s (%s) sucessful loaded", moduleName, moduleMain));
			}
			catch (final NullPointerException npe)
			{
				EricPackage.logException("Unable to load module '" + moduleName + "'", npe);
			}
	}

	public static final void unload(final String moduleName)
	{
		final String moduleMain = config.getString(moduleName + CONFIG_MODULE_MAIN_DIR);
		if (moduleMain != null)
			try
			{
				final Module module = getByName(moduleName);
				if (module != null)
				{
					moduleMapping.remove(moduleName);

					module.setModuleEnabled(false);

					module.unload();

					if (module.getModuleCommands() != null)
						CommandManager.unregisterHandler(module.getModuleCommands());

					EricPackage.info(String.format("Function %s unloaded", moduleName));
				}
				else
					EricPackage.info(String.format("Function %s is doesn't exists. It seems it's already unloaded.", moduleName));
			}
			catch (final NullPointerException npe)
			{
				EricPackage.logException("Unable to unload module '" + moduleName + "'", npe);
			}
	}

	public static final void unloadall()
	{
		for (final String moduleName : config.getKeys(false))
		{
			final String moduleMain = config.getString(moduleName + CONFIG_MODULE_MAIN_DIR, null);
			if (moduleMain != null)
				try
				{
					final Module module = getByName(moduleName);
					if (module != null)
					{
						module.setModuleEnabled(false);

						moduleMapping.remove(moduleName);

						module.unload();

						if (module.getModuleCommands() != null)
							CommandManager.unregisterHandler(module.getModuleCommands());

						EricPackage.info(String.format("[Eric Package] Function %s (%s) sucessful unloaded", moduleName, moduleMain));
					}
					else
						EricPackage.info(String.format("[Eric Package] Function %s (%s) is already unloaded.", moduleName, moduleMain));
				}
				catch (final NullPointerException npe)
				{
					EricPackage.logException("Unable to unload module", npe);
				}
		}
	}
	// </editor-fold>

	// <editor-fold desc="Module event handling methods">
	@Override
	public final boolean checkHandlerAndProcessEvent(final Event event, final int eventDataIndex)
	{
		if (moduleEnabled)
			return onEvent(event, eventDataIndex);
		return false;
	}
	// </editor-fold>

	@Override
	public boolean onCommand(final CommandSender s, final String l, final String[] d)
	{
		return false;
	}
}
