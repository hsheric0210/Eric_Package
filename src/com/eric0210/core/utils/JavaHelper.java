package com.eric0210.core.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;

import com.eric0210.core.EricPackage;

public class JavaHelper
{
	public static final <T, E> T getKey(final Map<T, E> map, final E value)
	{
		if (map.containsValue(value))
			for (final Entry<T, E> e : map.entrySet())
				if (e.getValue().equals(value))
					return e.getKey();
		return null;
	}

	public static final <T> int IndexOf(final Collection<T> collection, final T value)
	{
		int index = 0;
		for (final T val : collection)
		{
			if (val.equals(value))
				return index;
			index++;
		}
		return -1;
	}

	public static final void setField(final Object instance, final String field, final Object data)
	{
		try
		{
			final Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, data);
			f.setAccessible(false);
		}
		catch (final NoSuchFieldException | IllegalAccessException e)
		{
			EricPackage.logException("Can't set the field " + field + " in class " + instance.getClass().getCanonicalName(), e);
		}
	}

	public static final <T> T getField(final Object instance, final String field)
	{
		Object result = null;
		try
		{
			final Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			result = f.get(instance);
			f.setAccessible(false);
		}
		catch (final NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
		{
			EricPackage.logException("Can't get the field " + field + " in class " + instance.getClass().getCanonicalName(), e);
		}
		return (T) result;
	}

	public static final boolean isFieldExist(final Object instance, final String field)
	{
		try
		{
			instance.getClass().getDeclaredField(field);
		}
		catch (final NoSuchFieldException | SecurityException ignored)
		{
			return false;
		}

		return true;
	}

	public static final String randomString(final int length)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append((char) JavaHelper.random(Character.MIN_VALUE, Character.MAX_VALUE));
		return sb.toString();
	}

	public static final int random(final int origin, final int bound)
	{
		final int n = bound - origin;
		if (n > 0)
			return new Random().nextInt(n) + origin;
		int r;
		do
			r = new Random().nextInt();
		while (r < origin || r >= bound);
		return r;
	}

	public static final double random(final double origin, final double bound)
	{
		double r = new Random().nextDouble();
		if (origin < bound)
		{
			r = r * (bound - origin) + origin;
			if (r >= bound) // correct for rounding
				r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
		}
		return r;
	}

	public static final Properties loadProperties(final File f)
	{
		try
		{
			final Properties p = new Properties();
			p.load(new FileReader(f));
			return p;
		}
		catch (final IOException ioe)
		{
			EricPackage.logException("Unable to load properties " + f, ioe);
		}

		return null;
	}

	public static final String drawProgressBar(final int max, final int current)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= max; i++)
			if (i > current)
				sb.append("\u2591");
			else
				sb.append("\u2588");
		return sb.toString();
	}
}
