package com.eric0210.core.api.item;

public class IllegalItemTypeException extends Exception
{
	private static final long serialVersionUID = 8977034753258541612L;

	public IllegalItemTypeException()
	{

	}

	public IllegalItemTypeException(final String msg)
	{
		super(msg);
	}

	public IllegalItemTypeException(final String msg, final Throwable exception)
	{
		super(msg, exception);
	}

	public IllegalItemTypeException(final Throwable exception)
	{
		super(exception);
	}
}
