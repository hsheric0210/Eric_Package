package com.eric0210.core.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eric0210.core.EricPackage;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
	private final String configName;
	private final File dataFolder;
	private File file;

	public Config(final String configName, final File dataFolder)
	{
		this.configName = configName;
		this.dataFolder = dataFolder;
		try
		{
			final File configFile = new File(dataFolder, configName + ".yml");
			if (!configFile.exists())
				configFile.createNewFile();

			file = configFile;

			// FIXME: What is this?
			final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			config.set("enabled", true);
			config.save(file);
		}
		catch (final IOException ioe)
		{
			EricPackage.logException("Can't create the config " + configName + " in folder " + dataFolder, ioe);
		}

	}

	// <editor-fold desc="Save/Load">
	public YamlConfiguration getYAML()
	{
		return YamlConfiguration.loadConfiguration(file);
	}

	public void save(final YamlConfiguration yaml)
	{
		try
		{
			yaml.save(file);
		}
		catch (final IOException ioe)
		{
			EricPackage.logException("Can't save the config " + configName + " in folder " + dataFolder, ioe);
		}
	}
	// </editor-fold>

	public File getFile()
	{
		return file;
	}

	// <editor-fold desc="Set">
	public void set(final String key, final Object value)
	{
		try
		{
			final YamlConfiguration config = getYAML();
			config.set(key, value);
			config.save(file);
		}
		catch (final IOException t)
		{
			EricPackage.logException("Can't save the config " + configName + " in folder " + dataFolder, t);
		}
	}

	public <T> void setList(final String key, final List<T> values)
	{
		try
		{
			final YamlConfiguration config = getYAML();
			config.set(key, Collections.unmodifiableList(values));
			config.save(file);
		}
		catch (final IOException t)
		{
			EricPackage.logException("Can't save the config " + configName + " in folder " + dataFolder, t);
		}
	}
	// </editor-fold>

	// <editor-fold desc="Get">
	public Object getNoCreate(final String key)
	{
		final YamlConfiguration config = getYAML();
		return config.get(key);
	}

	public <T> T get(final String key, final T def)
	{
		final YamlConfiguration config = getYAML();
		T ret = (T) config.get(key);
		if (ret == null)
			try
			{
				config.set(key, def);
				config.save(file);
				ret = def;
			}
			catch (final IOException ioe)
			{
				EricPackage.logException("Can't save the config " + configName + " in folder " + dataFolder, ioe);
			}
		return ret;
	}

	public <T> List<T> getListNoCreate(final String key, final Class<T> listClass)
	{
		final YamlConfiguration config = getYAML();
		try
		{
			return (List<T>) config.getList(key, null);
		}
		catch (final ClassCastException cce)
		{
		}

		return new ArrayList<>();
	}

	public <T> List<T> getList(final String key, final List<T> def)
	{
		final YamlConfiguration conf = getYAML();
		List<T> ret = (List<T>) conf.get(key);
		if (ret == null)
			try
			{
				conf.set(key, def);
				conf.save(file);
				ret = def;
			}
			catch (final IOException ioe)
			{
				EricPackage.logException("Can't save the config " + configName + " in folder " + dataFolder, ioe);
			}
		return ret;
	}
	// </editor-fold>
}
